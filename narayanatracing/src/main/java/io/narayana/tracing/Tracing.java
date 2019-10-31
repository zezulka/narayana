package io.narayana.tracing;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.logging.Logger;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.tag.Tag;
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
 * Three classes should be taken into consideration: i) SpanHandle, an
 * abstraction of the OpenTracing span providing only the {@code close} method
 * with which the user declares that the span is no longer active and should be
 * reported; all the other functionality is provided via static methods of this
 * class
 *
 * ii) RootSpanHandleBuilder, responsible for building a SpanHandle which
 * represents the root of a transaction trace, all other spans created under
 * Narayana will be (direct or indirect) children of this span
 *
 * iii) SpanHandleBuilder - responsible for building regular spans
 *
 * Note: spans are always activated at the point of span creation (we tightly couple
 * the events again because of the goal of having a thin API).
 *
 * @author Miloslav Zezulka (mzezulka@redhat.com)
 */
public class Tracing {

    // transaction ID -> its relevant root span (of the whole trace)
    private static final ConcurrentMap<String, Span> TX_UID_TO_SPAN = new ConcurrentHashMap<>();
    /*
     * transaction ID -> wrapping span of all the actions preceding the actual
     * execution of 2PC
     */
    private static final ConcurrentMap<String, Span> TX_UID_TO_PRE2PC_SPAN = new ConcurrentHashMap<>();
    private static final Logger LOG = Logger.getLogger(Tracing.class);

    /**
     * Build a new root span handle which represents the whole transaction. Any
     * other span handles created in the Narayana code base should be attached to
     * this root scope using the "ordinary" SpanBuilder.
     *
     * Usage: as the transaction will always begin and end at two different methods,
     * we need to manage (de)activation of the span manually, schematically like
     * this:
     *
     * <pre>
     * <code>public void transactionStart(Uid uid, ...) {
     *     new SpanBuilder(SpanName.TX_BEGIN, uid)
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
     * Note: we're not using the Uid class as this would create a cyclic dependency
     * between narayanatracing arjuna modules. Strings (which should always
     * represent a Uid!) are used instead.
     */
    public static class RootSpanBuilder {

        private SpanBuilder spanBldr;
        private SpanBuilder pre2PCspanBldr;

        public RootSpanBuilder(Object... args) {
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
            Objects.requireNonNull(name, "Name of the tag cannot be null");
            spanBldr = spanBldr.withTag(name.toString(), val == null ? "null" : val.toString());
            return this;
        }

        public <T> RootSpanBuilder tag(Tag<T> tag, T value) {
            Objects.requireNonNull(tag, "Tag cannot be null.");
            spanBldr = spanBldr.withTag(tag, value);
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
            Span rootSpan = spanBldr.withTag(Tags.COMPONENT, "narayana").ignoreActiveSpan().start();
            TX_UID_TO_SPAN.put(txUid, rootSpan);
            getTracer().scopeManager().activate(rootSpan);

            pre2PCspanBldr.asChildOf(rootSpan);
            Span pre2PCSpan = pre2PCspanBldr.withTag(Tags.COMPONENT, "narayana").start();
            TX_UID_TO_PRE2PC_SPAN.put(txUid, pre2PCSpan);

            return pre2PCSpan;
        }
    }

    /**
     * Responsibility of this class: create a new span handle. When building it,
     * make sure that the appropriate root span has already been created.
     *
     * Example of usage:
     *
     * <pre>
     * <code>SpanHandle handle = new SpanHandleBuilder(SpanName.XYZ)
     *    .tag(TagName.UID, get_uid())
     *    .build(get_uid().toString());
     * try (Scope s = Tracing.activateHandle(handle)) {
     *     // this is where 's' is active
     * } finally {
     *     handle.finish();
     * }
     * </code>
     * </pre>
     */
    public static class DefaultSpanBuilder {

        private SpanBuilder spanBldr;
        private SpanName name;

        public DefaultSpanBuilder(SpanName name, Object... args) {
            this.spanBldr = prepareSpan(name, args);
            this.name = name;
        }

        private static SpanBuilder prepareSpan(SpanName name, Object... args) {
            Objects.requireNonNull(name, "Name of the span cannot be null");
            return getTracer().buildSpan(String.format(name.toString(), args));
        }

        /**
         * Adds tag to the started span and simply calls the {@code toString} method on
         * {@code val}.
         */
        public DefaultSpanBuilder tag(TagName name, Object val) {
            Objects.requireNonNull(name, "Name of the tag cannot be null");
            spanBldr = spanBldr.withTag(name.toString(), val == null ? "null" : val.toString());
            return this;
        }

        public <T> DefaultSpanBuilder tag(Tag<T> tag, T value) {
            Objects.requireNonNull(tag, "Tag cannot be null.");
            spanBldr = spanBldr.withTag(tag, value);
            return this;
        }

        /**
         * Build a regular span and attach it to the transaction with id {@code txUid}.
         *
         * Note: if the "real" part of a transaction processing hasn't started yet and
         * the transaction manager needs to do some preparations (i.e. XAResource
         * enlistment), the trace is in a pseudo "pre-2PC" state where every span is
         * hooked to a SpanName.GLOBAL_PRE_2PC span (which is always a child of a root
         * span).
         *
         * @param txUid id of a transaction which already has a root span
         * @throws IllegalStateException no appropriate span for {@code txUid} exists
         * @return {@code SpanHandle} with a started span
         */
        public Span build(String txUid) {
            Span pre2PCSpan = TX_UID_TO_PRE2PC_SPAN.get(txUid);
            Span parent = pre2PCSpan == null ? TX_UID_TO_SPAN.get(txUid) : pre2PCSpan;

            // we're either outside of trace reach now or the trace has never been
            // registered - the span will be reported but not to an existing trace
            if (parent == null) {
                LOG.warnf(
                        "There was an attempt to build span belonging to tx '%s' but no root span registered for it found! Span name: '%s', span map: '%s'",
                        txUid, name, TX_UID_TO_SPAN);
            }
            return spanBldr.asChildOf(parent).withTag(Tags.COMPONENT, "narayana").start();
        }

        /**
         * Build a regular span and attach it to the transaction with id {@code txUid}.
         *
         * It is expected that the trace is currently on an entirely different node
         * and tracing context needs to be extracted from a remote party and then this
         * context {@code spanContext} will be used as a "gluing" span
         * for the spans in the remote (coordinating) and spans on the node calling this
         * method, presumably a node on which a subordinate transaction is executed.
         *
         * If the span has already been registered, we only retrieve the existing span.
         *
         * @return {@code SpanHandle} with a started span
         */
        public Span buildSubordinateIfAbsent(String txUid, SpanContext spanContext) {
            Span span = TX_UID_TO_SPAN.get(txUid);
            if(span != null) {
                return span;
            }
            Objects.requireNonNull(spanContext);
            spanBldr = spanBldr.asChildOf(spanContext);
            span = spanBldr.withTag(Tags.COMPONENT, "narayana").start();
            TX_UID_TO_SPAN.put(txUid, span);
            return span;
        }

        /**
         * Build a span which has no explicit parent set. Useful for creating nested
         * traces. See the OpenTracing documentation on what the implicit reference is.
         *
         * @throws IllegalStateException there is currently no active span
         */
        public Span build() {
            if (!activeSpan().isPresent()) {
                throw new IllegalStateException(String
                        .format("The span '%s' could not be nested into enclosing span because there is none.", name));
            }
            return spanBldr.withTag(Tags.COMPONENT, "narayana").start();
        }
    }

    public static Scope activateSpan(Span span) {
        return getTracer().activateSpan(span);
    }

    private Tracing() {
    }
    /*
     * This method switches from the "pre-2PC" phase to the protocol phase.
     */
    public static void begin2PC(String txUid) {
        Span span = TX_UID_TO_PRE2PC_SPAN.remove(txUid);
        if (span != null)
            span.finish();
    }

    private static void finish(String txUid, boolean remove) {
        // We need to check for superfluous calls to this method
        Span span = remove ? TX_UID_TO_SPAN.remove(txUid) : TX_UID_TO_SPAN.get(txUid);
        if (span != null)
            span.finish();
    }

    /**
     * Finishes the root span representing the transaction with id {@code txUid}
     *
     * @param txUid
     */
    public static void finish(String txUid) {
        finish(txUid, false);
    }

    /**
     * Finishes the root span but still keeps it in the collection, making it
     * possible to attach async spans (which are outside of the reach of the trace).
     *
     * @param txUid
     */
    public static void finishWithoutRemoval(String txUid) {
        finish(txUid, false);
    }

    /**
     * This is different from setting the transaction status as failed. Using this
     * method, the span itself in terms of opentracing is marked as failed.
     *
     */
    public static void markTransactionFailed(String txUid) {
        Span span = TX_UID_TO_SPAN.get(txUid);
        if (span != null)
            span.setTag(Tags.ERROR, true);
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
        Span span = TX_UID_TO_SPAN.get(txUid);
        if (span != null)
            span.setTag(TagName.STATUS.toString(), status.toString().toLowerCase());
    }

    /**
     * Sets tag which for a span which is currently activated by the scope manager.
     * Useful when a user wishes to add tags whose existence / value is dependent on
     * the context (i.e. status of the transaction inside of the method call).
     */
    public static void addTag(TagName name, String val) {
        activeSpan().ifPresent(s -> s.setTag(name.toString(), val));
    }

    public static void addTag(TagName name, Object obj) {
        addTag(name, obj == null ? "null" : obj.toString());
    }

    public static <T> void addTag(Tag<T> tag, T obj) {
        activeSpan().ifPresent((s) -> s.setTag(tag, obj));
    }

    /**
     * Log a message for the currently active span.
     */
    public static void log(String message) {
        activeSpan().ifPresent(s -> s.log(message));
    }

    public static <T> void log(String fld, String value) {
        activeSpan().ifPresent(s -> s.log(Collections.singletonMap(fld, value)));
    }

    private static Optional<Span> activeSpan() {
        Span span = getTracer().activeSpan();
        return span == null ? Optional.empty() : Optional.of(span);
    }

    /**
     * This is package private on purpose. Users of the tracing module shouldn't be
     * encumbered with tracers.
     *
     * @return registered tracer or any default tracer provided by the opentracing
     *         implementation
     */
    static Tracer getTracer() {
        return GlobalTracer.get();
    }
}