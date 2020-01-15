package io.narayana.tracing;

import java.util.Objects;
import java.util.Optional;

import io.narayana.tracing.names.SpanName;
import io.narayana.tracing.names.TagName;
import io.narayana.tracing.names.TransactionStatus;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;

/**
 * Class enabling to utilise tracing in the code by providing an instantiated
 * and setup Tracer class.
 *
 * Instead of accessing the tracing code directly, much of the work is done
 * "behind the scenes" in this class and the intention is to provide as thin API
 * to the user as possible.
 *
 * Most of the opentracing functionality is to be accessed from this utility
 * class.
 *
 * Note: spans are always activated at the point of span creation (we tightly
 * couple the events again because of the goal of having a thin API).
 *
 * @author Miloslav Zezulka (mzezulka@redhat.com)
 */
public class TracingUtils {
    static final boolean TRACING_ACTIVATED = Boolean
            .valueOf(System.getProperty("org.jboss.narayana.tracingActivated", "true"));
    static final Scope DUMMY_SCOPE = new DummyScope();

    private TracingUtils() {
    }

    public static Scope activateSpan(Span span) {
        if (!TRACING_ACTIVATED) return DUMMY_SCOPE;
        return getTracer().activateSpan(span);
    }

    /*
     * This method switches from the "pre-2PC" phase to the protocol phase.
     *
     * It is possible (and highly probable) that this method will be called more
     * than once during a transaction's existence.
     *
     * Therefore, we always must explicitly check that if a root span with {@code
     * txUid} still exists but there is none corresponding in the pre2pc registry,
     * we must have already called therefore can simply ignore such calls.
     */
    public static void begin2PC(String txUid) {
        if (!TRACING_ACTIVATED) return;
        SpanRegistry.removePre2pc(txUid).ifPresent(s -> s.finish());
        if (!SpanRegistry.getRoot(txUid).isPresent()) {
            throw new IllegalStateException(String.format("Pre2PC span with id '%s' was deactivated but no corresponding parent span found.", txUid));
        }
    }

    private static void finish(String txUid, boolean remove) {
        if (!TRACING_ACTIVATED) return;
        // We need to check for superfluous calls to this method
        Optional<Span> span = remove ? SpanRegistry.removeRoot(txUid) : SpanRegistry.getRoot(txUid);
        span.ifPresent(s -> s.finish());
    }

    /**
     * Starts a new root span of a trace representing one distributed transaction.
     *
     * @param txUid string representation of the transaction
     */
    public static void start(String txUid) {
        Objects.requireNonNull(txUid);
        new RootSpanBuilder().tag(TagName.UID, txUid).build(txUid);
    }

    /**
     * Build a new root span which represents the whole transaction. Any
     * other span handles created in the Narayana code base should be attached to
     * this root scope using the "ordinary" SpanBuilder.
     *
     * Usage: as the transaction will always begin and end at two different methods,
     * we need to manage (de)activation of the span manually, schematically like
     * this:
     *
     * <pre>
     * <code>public void transactionStart(Uid uid, ...) {
     *     new RootSpanBuilder(uid)
     *            ...
     *           .build(uid);
     *     ...
     *}
     *
     *public void transactionEnd(Uid uid, ...) {
     *     Tracing.finish(uid);
     *     ...
     * </code>}
     * </pre>
     *
     * Note: we're not using the Uid class as a key as this would create a cyclic dependency
     * between narayanatracing arjuna modules. Strings (which should always
     * represent a Uid!) are used instead.
     */
    private static class RootSpanBuilder {

        private SpanBuilder spanBldr;
        private SpanBuilder pre2PCspanBldr;

        RootSpanBuilder(Object... args) {
            if(!TRACING_ACTIVATED) return;
            spanBldr = prepareSpan(SpanName.TX_ROOT, args).withTag(Tags.ERROR, false);
            pre2PCspanBldr = prepareSpan(SpanName.GLOBAL_ENLISTMENTS, args);
        }

        private static SpanBuilder prepareSpan(SpanName name, Object... args) {
            Objects.requireNonNull(name, "Name of the span cannot be null");
            return getTracer().buildSpan(String.format(name.toString(), args));
        }

        /**
         * Adds tag to the started span.
         */
        public RootSpanBuilder tag(TagName name, Object val) {
            if(!TRACING_ACTIVATED) return this;
            Objects.requireNonNull(name, "Name of the tag cannot be null");
            spanBldr = spanBldr.withTag(name.toString(), val == null ? "null" : val.toString());
            return this;
        }

        /**
         * Build the root span and propagate it as a handle. Any possible active
         * (=parent) spans are ignored as this is the root of a new transaction trace.
         *
         * @throws IllegalArgumentException {@code txUid} is null or a span with this ID
         *                                  already exists
         * @param txUid UID of the new transaction
         * @return
         */
        public Span build(String txUid) {
            if(!TRACING_ACTIVATED) return null;
            Span rootSpan = spanBldr.withTag(Tags.COMPONENT, "narayana").ignoreActiveSpan().start();
            SpanRegistry.insertRoot(txUid, rootSpan);
            getTracer().scopeManager().activate(rootSpan);

            Span pre2PCSpan = pre2PCspanBldr.asChildOf(rootSpan)
                                            .withTag(Tags.COMPONENT, "narayana")
                                            .start();
            SpanRegistry.insertPre2pc(txUid, pre2PCSpan);
            getTracer().scopeManager().activate(pre2PCSpan);

            return pre2PCSpan;
        }
    }

    /**
     * Finishes the root span representing the transaction with id {@code txUid}
     *
     * @param txUid string representation of the transaction
     */
    public static void finish(String txUid) {
        if (!TRACING_ACTIVATED) return;
        Objects.requireNonNull(txUid);
        finish(txUid, true);
    }

    /**
     * Finishes the root span but still keeps it in the collection, making it
     * possible to attach async spans (which are outside of the reach of the trace).
     *
     * @param txUid
     */
    public static void finishWithoutRemoval(String txUid) {
        if (!TRACING_ACTIVATED) return;
        finish(txUid, false);
    }

    /**
     * Mark the span itself as failed in terms of opentracing.
     * Hence this is different from setting the transaction
     * status span tag as failed via calling {@code setTransactionStatus}.
     */
    public static void markTransactionFailed(String txUid) {
        if (!TRACING_ACTIVATED) return;
        SpanRegistry.getRoot(txUid).ifPresent(s -> s.setTag(Tags.ERROR, true));
    }

    /**
     * Sets TagName.STATUS tag of the root span. If this method is called more than
     * once, the value is overwritten.
     *
     * @throws IllegalArgumentException {@code txUid} does not represent any
     *                                  currently managed transaction
     * @param txUid  UID of the transaction
     * @param status one of the possible states any transaction could be in
     */
    public static void setTransactionStatus(String txUid, TransactionStatus status) {
        if (!TRACING_ACTIVATED) return;
        SpanRegistry.getRoot(txUid)
                .ifPresent(s -> s.setTag(TagName.STATUS.toString(), status.toString().toLowerCase()));
    }

    /**
     * Sets tag which for a span which is currently activated by the scope manager.
     * Useful when a user wishes to add tags whose existence / value is dependent on
     * the context (i.e. status of the transaction inside of the method call).
     */
    public static void addTag(TagName name, String val) {
        if (!TRACING_ACTIVATED) return;
        activeSpan().ifPresent(s -> s.setTag(name.toString(), val));
    }

    /**
     * Log a message for the currently active span.
     */
    public static void log(String message) {
        if (!TRACING_ACTIVATED) return;
        activeSpan().ifPresent(s -> s.log(message));
    }

    static Optional<Span> activeSpan() {
        if (!TRACING_ACTIVATED) return Optional.empty();
        Span span = getTracer().activeSpan();
        return span == null ? Optional.empty() : Optional.of(span);
    }

    /**
     * @return registered tracer or any default tracer provided by the opentracing
     *         implementation
     */
    static Tracer getTracer() {
        // when tracing is deactivated,
        // no tracer code should be called
        if (!TRACING_ACTIVATED) return null;
        return GlobalTracer.get();
    }
}
