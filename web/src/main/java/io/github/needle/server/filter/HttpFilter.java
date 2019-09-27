package io.github.needle.server.filter;

import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public interface HttpFilter {

	boolean onRequest(HttpRequest request, HttpResponse response);

	default boolean onResponse(HttpRequest request, HttpResponse response) {
		return true;
	}

}
