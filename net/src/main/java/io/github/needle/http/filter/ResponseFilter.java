package io.github.needle.http.filter;

import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

@FunctionalInterface
public interface ResponseFilter extends HttpFilter {

	@Override
	boolean onResponse(HttpRequest request, HttpResponse response);

	@Override
	default boolean onRequest(HttpRequest request, HttpResponse response) { return true; }

}
