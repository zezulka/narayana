package io.narayana.tracing;

import java.io.IOException;
import java.util.Objects;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer.SpanBuilder;
import io.opentracing.tag.Tags;

/**
 * Create a new span and activate it under new active scope.
 * 
 * 
 * Note: it is important to call the Scope.close() method (Scope implements
 * java.io.Closeable, so it is highly recommended to use the try-with-resources
 * idiom whenever possible)
 */
public final class ScopeBuilder {

	private SpanBuilder spanBldr;

	/**
	 * @param name   name of the span which will be activated in the scope
	 * @param params optional parameters for SpanName instances which have formatter
	 *               templates instead of "ready" string constants
	 */
	public ScopeBuilder(SpanName name, Object... params) {
		this.spanBldr = prepareSpan(String.format(name.toString(), params));
	}

	/**
	 * Adds tag to the started span.
	 */
	public ScopeBuilder tag(TagNames name, Object val) {
		Objects.requireNonNull(name, "Name of the tag cannot be null");
		spanBldr = spanBldr.withTag(name.toString(), val == null ? "null" : val.toString());
		return this;
	}

	/**
	 * Construct a span with a special Narayana-specific component tag "narayana"
	 */
	private static SpanBuilder prepareSpan(String name) {
		Objects.requireNonNull(name, "Name of the span cannot be null");
		return TracingUtils.getTracer().buildSpan(name);
	}

	public Scope start() {
		return TracingUtils.getTracer().scopeManager().activate(spanBldr.withTag(Tags.COMPONENT, "narayana").start(), true);
	}
}
