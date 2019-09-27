package io.github.needle.server.router.context;

import io.github.kits.Maps;
import io.github.needle.bean.Singleton;
import io.github.needle.http.enums.HttpMethod;
import io.github.needle.http.router.HttpRouter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class DirectRouterContext extends RouterContext {

	private volatile Map<HttpMethod, Map<String, HttpRouter>> ROUTERS;

	private DirectRouterContext() {
		ROUTERS = super.getDirectRouters();
	}

	static DirectRouterContext getInstance() {
		Singleton.register(DirectRouterContextHolder.INSTANCE);
		return DirectRouterContextHolder.INSTANCE;
	}

	@Override
	public Optional<HttpRouter> getRouter(HttpMethod method, String path) {
		Map<String, HttpRouter> routerHandlerMap = ROUTERS.get(method);
		if (Maps.isNullOrEmpty(routerHandlerMap))
			return Optional.empty();
		return Optional.ofNullable(routerHandlerMap.get(path));
	}

	@Override
	public void addRouter(HttpMethod method, String path, HttpRouter router) {
		Map<String, HttpRouter> methodRouter = ROUTERS.get(method);
		if (Objects.isNull(methodRouter)) {
			methodRouter = new ConcurrentHashMap<>();
			ROUTERS.put(method, methodRouter);
		}
		methodRouter.put(path, router);
	}

	private static class DirectRouterContextHolder {
		private static final DirectRouterContext INSTANCE = new DirectRouterContext();
	}

}
