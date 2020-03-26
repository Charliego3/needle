package io.github.needle.http.filter;

import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

@FunctionalInterface
public interface RequestFilter extends HttpFilter {

	@Override
	boolean onRequest(HttpRequest request, HttpResponse response);

	@Override
	default boolean onResponse(HttpRequest request, HttpResponse response) { return true; }

}
