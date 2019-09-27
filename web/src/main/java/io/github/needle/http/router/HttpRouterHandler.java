package io.github.needle.http.router;

import io.github.kits.Envs;
import io.github.kits.Lists;
import io.github.kits.Strings;
import io.github.kits.log.Logger;
import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;
import io.github.needle.http.protocol.HttpContentType;
import io.github.needle.server.router.context.RouterContext;
import io.github.needle.server.router.context.RouterContextFactory;
import io.github.needle.annotations.Controller;
import io.github.needle.annotations.RestController;
import io.github.needle.annotations.Router;
import io.github.needle.bean.Singleton;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpRouterHandler implements HttpRouter {

	private Class<?> clazz;
	private Method   method;
	private Router   router;
	private boolean  singleTon;
	private boolean  isRest;

	public HttpRouterHandler() {
	}

	public HttpRouterHandler(Class clazz, Method method, Router router, boolean singleTon, boolean isRest) {
		this.clazz = clazz;
		this.method = method;
		this.router = router;
		this.singleTon = singleTon;
		this.isRest = isRest;
	}

	public void registerRouter(String classpath) throws IOException {
		List<Class> routers = Envs.getDirClass(new File(classpath), Controller.class, RestController.class);

		RouterContext.functionRouter().forEach((method, path) ->
				Logger.infof("Scan Functional Router: {} {} {}",
						Strings.indentRight(method.name(), 7), "->", path)
		);

		if (Lists.isNotNullOrEmpty(routers)) {
			Annotation controller;
			for (Class clazz : routers) {
				// Controller
				controller = clazz.getAnnotation(Controller.class);
				if (Objects.nonNull(controller)) {
					addRouter(controller, clazz);
					continue;
				}

				// RestController
				controller = clazz.getAnnotation(RestController.class);
				if (Objects.nonNull(controller)) {
					addRouter(controller, clazz);
				}
			}
		}
	}

	private void addRouter(Annotation controller, Class clazz) {
		String  basePath;
		boolean singleton = false;
		boolean rest      = false;
		if (controller instanceof Controller) {
			basePath = ((Controller) controller).value();
			singleton = ((Controller) controller).singleton();
		} else if (controller instanceof RestController) {
			basePath = ((RestController) controller).value();
			singleton = ((RestController) controller).singleton();
			rest = true;
		} else
			basePath = "/";
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Router router = method.getAnnotation(Router.class);
			if (Objects.nonNull(router)) {
				String path = basePath;
				if (!basePath.equals("/")) {
					if (!basePath.startsWith("/"))
						path = "/" + basePath;
					if (!basePath.endsWith("/"))
						path += "/";
				}
				path += router.value();
				if (path.endsWith("/"))
					path = path.substring(0, path.length() - 1);
				path = Strings.replace(path, "/", "//");
				RouterContext routerContext = RouterContextFactory.getContext(path);
				routerContext.addRouter(router.method(), path,
						new HttpRouterHandler(clazz, method, router, singleton, rest));
				Logger.infof("Scan Annotation Router: {} {} {}",
						Strings.indentRight(router.method().name(), 7), "->", path);
			}
		}
	}

	@Override
	public void process(HttpRequest request, HttpResponse response) {
		try {
			Object object;
			if (singleTon)
				object = Singleton.getInstance(clazz);
			else
				object = clazz.newInstance();
			Object invoke = this.method.invoke(object, request.getParameterAsString("name").orElse("参数为空"), 23);
			Logger.infof("Router invoke response: {}", invoke.toString());
			response.write(invoke.toString(), isRest ? HttpContentType.JSON : router.contentType());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			Logger.errorf("Router invoke error, Path: {}", e, request.getPath());
		} catch (Exception e) {
			Logger.errorf("Router process error, Path: {}", e, request.getPath());
		}
	}

}
