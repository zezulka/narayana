package io.narayana.tracing;

import static io.narayana.tracing.TracingTestUtils.operationEnumsToStrings;
import static io.narayana.tracing.TracingTestUtils.spansToOperationStrings;
import static io.narayana.tracing.TracingUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.narayana.tracing.names.SpanName;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.mock.MockTracer;
import io.opentracing.util.GlobalTracer;

/**
 * Unit tests for the opentracing Narayana facade.
 * @author Miloslav Zezulka (mzezulka@redhat.com)
 *
 */
public class TracingTest {

    private static MockTracer testTracer = new MockTracer();
    private static final String TEST_ROOT_UID = "TEST-ROOT";

    @BeforeClass
    public static void init() {
        // we've successfully registered our mock tracer (the flag tells us exactly that)
        assertThat(GlobalTracer.registerIfAbsent(testTracer)).isTrue();
    }

    @Before
    public void prepare() {
        SpanRegistry.reset();
    }

    @After
    public void teardown() {
        testTracer.reset();
    }

    @Test
    public void simpleTrace() {
        start(TEST_ROOT_UID);
        finish(TEST_ROOT_UID);
        List<String> opNamesExpected = operationEnumsToStrings(SpanName.TX_ROOT);
        assertThat(spansToOperationStrings(testTracer.finishedSpans())).isEqualTo(opNamesExpected);
    }

    @Test(expected = Test.None.class /* no exception is expected to be thrown */)
    public void simpleTraceFinishTwice() {
        start(TEST_ROOT_UID);
        finish(TEST_ROOT_UID);
        finish(TEST_ROOT_UID);
    }

    @Test(expected = Test.None.class /* no exception is expected to be thrown */)
    public void simpleTraceFinishTwoTransactionsInSeries() {
        String firstUid = TEST_ROOT_UID + "1";
        String secondUid = TEST_ROOT_UID + "2";
        start(firstUid);
        finish(firstUid);
        start(secondUid);
        finish(secondUid);
    }

    @Test(expected = Test.None.class /* no exception is expected to be thrown */)
    public void simpleTraceFinishTwoTransactionsInterleaved() {
        String firstUid = TEST_ROOT_UID + "1";
        String secondUid = TEST_ROOT_UID + "2";
        start(firstUid);
        start(secondUid);
        finish(firstUid);
        finish(secondUid);
    }

    @Test(expected = IllegalArgumentException.class /* as per contract */)
    public void simpleTraceFinishTwoSameName() {
        start(TEST_ROOT_UID);
        start(TEST_ROOT_UID);
    }

    @Test
    public void simpleTraceFinishWithoutRemovalAndThenFinish() {
        start(TEST_ROOT_UID);
        finishWithoutRemoval(TEST_ROOT_UID);
        List<String> opNamesExpected = operationEnumsToStrings(SpanName.TX_ROOT);
        assertThat(spansToOperationStrings(testTracer.finishedSpans())).isEqualTo(opNamesExpected);
        finish(TEST_ROOT_UID);
    }

    @Test
    public void nestedSpansSimple() {
        start(TEST_ROOT_UID);
        begin2PC(TEST_ROOT_UID);
        finish(TEST_ROOT_UID);
        List<String> opNamesExpected = operationEnumsToStrings(SpanName.GLOBAL_ENLISTMENTS, SpanName.TX_ROOT);
        assertThat(spansToOperationStrings(testTracer.finishedSpans())).isEqualTo(opNamesExpected);
    }

    @Test
    public void nestedSpans() {
        start(TEST_ROOT_UID);
        begin2PC(TEST_ROOT_UID);
        Span span = new NarayanaSpanBuilder(SpanName.GLOBAL_PREPARE).build(TEST_ROOT_UID);
        try (Scope _s = TracingUtils.activateSpan(span)) {
            //no-op
        } finally {
            span.finish();
            finish(TEST_ROOT_UID);
        }
        List<String> opNamesExpected = operationEnumsToStrings(SpanName.GLOBAL_ENLISTMENTS, SpanName.GLOBAL_PREPARE, SpanName.TX_ROOT);
        assertThat(spansToOperationStrings(testTracer.finishedSpans())).isEqualTo(opNamesExpected);
    }
}
