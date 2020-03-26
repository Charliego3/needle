package io.github.needle.http;

public interface Mux {

	void handle(HttpMethod method, String pattern, HttpHandler handler);

	HttpHandler search(HttpMethod method, String pattern);

}
