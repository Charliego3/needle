package io.github.needle.filter;

import io.github.kits.log.Logger;
import io.github.needle.annotations.Order;
import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;
import io.github.needle.server.filter.HttpFilter;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
@Order(value = 2)
public class FilterTest2 implements HttpFilter {

	@Override
	public boolean onRequest(HttpRequest request, HttpResponse response) {
		String path = request.getPath();
		Logger.infof("FilterTest2 path: {}", request.getPath());
		Logger.infof("FilterTest2 params: {}", request.getParameters());
		response.write("filter2 return!");
		return !path.equals("/restController1/method2");
	}

	@Override
	public boolean onResponse(HttpRequest request, HttpResponse response) {
		return true;
	}

}
