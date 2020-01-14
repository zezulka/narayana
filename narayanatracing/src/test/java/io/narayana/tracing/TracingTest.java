package io.narayana.tracing;

import static io.narayana.tracing.TracingTestUtils.operationEnumsToStrings;
import static io.narayana.tracing.TracingTestUtils.spansToOperationStrings;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import io.narayana.tracing.names.SpanName;
import io.narayana.tracing.names.TagName;
import io.opentracing.mock.MockSpan;
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

    @After
    public void teardown() {
        testTracer.reset();
    }

    @Test
    public void simpleTrace() {
        new RootSpanBuilder().tag(TagName.UID, TEST_ROOT_UID).build(TEST_ROOT_UID);
        TracingUtils.finish(TEST_ROOT_UID);
        List<String> opNamesExpected = operationEnumsToStrings(SpanName.TX_ROOT);
        List<MockSpan> spans = testTracer.finishedSpans();
        assertThat(spans.size()).isEqualTo(opNamesExpected.size());
        assertThat(spansToOperationStrings(spans)).isEqualTo(opNamesExpected);
    }

}
