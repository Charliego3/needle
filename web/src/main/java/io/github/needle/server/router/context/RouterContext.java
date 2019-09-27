package io.github.needle.server.router.context;

import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.router.HttpRouter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class RouterContext {

	private final static Map<HttpMethod, String> functionalRouter = new HashMap<>();

	private final static Map<RouterContextEnum, Map<HttpMethod, Map<String, HttpRouter>>> ROUTERS;

	Map<HttpMethod, Map<String, HttpRouter>> getDirectRouters() {
		Map<HttpMethod, Map<String, HttpRouter>> directRoutes = ROUTERS.get(RouterContextEnum.DIRECT);
		if (Objects.isNull(directRoutes)) {
			directRoutes = new ConcurrentHashMap<>();
			ROUTERS.put(RouterContextEnum.DIRECT, directRoutes);
		}
		return directRoutes;
	}

	Map<HttpMethod, Map<String, HttpRouter>> getRegexRouters() {
		Map<HttpMethod, Map<String, HttpRouter>> regexRouter = ROUTERS.get(RouterContextEnum.REGEX);
		if (Objects.isNull(regexRouter)) {
			regexRouter = new ConcurrentHashMap<>();
			ROUTERS.put(RouterContextEnum.REGEX, regexRouter);
		}
		return regexRouter;
	}

	public static Map<HttpMethod, String> functionRouter() {
		return functionalRouter;
	}

	public abstract Optional<HttpRouter> getRouter(HttpMethod method, String path);

	public abstract void addRouter(HttpMethod method, String path, HttpRouter router);

	static {
		ROUTERS = new ConcurrentHashMap<>();
	}

}
