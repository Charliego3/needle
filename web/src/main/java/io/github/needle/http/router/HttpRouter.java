package io.github.needle.http.router;

import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface HttpRouter {

	void process(HttpRequest request, HttpResponse response);

}
