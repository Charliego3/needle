package io.github.needle.server.router.context;

import io.github.kits.Assert;
import io.github.kits.Strings;
import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.HttpRequest;
import io.github.needle.http.router.HttpRouter;

import java.util.Optional;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class RouterContextFactory {

	public static RouterContext getContext(String path) {
		if (Strings.regexFind(path, "\\{.*\\}"))
			return RegexRouterContext.getInstance();
		return DirectRouterContext.getInstance();
	}

	public static DirectRouterContext getDirect() {
		return DirectRouterContext.getInstance();
	}

	public static RegexRouterContext getRegex() {
		return RegexRouterContext.getInstance();
	}

	public static void addRouter(HttpMethod method, String path, HttpRouter router) {
		Assert.isNotNullOrEmpty(path, "Register Router error! Router path is null. HttpMethod: " + method.name());
		Assert.isNotNull(router, "Register Router error! HttpRouter is null. HttpMethod: " + method.name() + ", Path: " + path);
		getContext(path).addRouter(method, path, router);
		RouterContext.functionRouter().put(method, path);
	}

	public static Optional<HttpRouter> getRouter(HttpRequest request) {
		return getContext(request.getPath()).getRouter(request.getMethod(), request.getPath());
	}

}
