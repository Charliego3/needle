package io.github.needle.http;

@FunctionalInterface
public interface HttpHandler {
	void process(HttpRequest request, HttpResponse response);
}
