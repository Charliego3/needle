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
@Order(value = 1)
public class FilterTest1 implements HttpFilter {

	@Override
	public boolean onRequest(HttpRequest request, HttpResponse response) {
		String path = request.getPath();
		Logger.infof("FilterTest1 path: {}", request.getPath());
		Logger.infof("FilterTest1 params: {}", request.getParameters());
		Logger.infof("FilterTest1 Request: {}", request);
		response.write("filter1 return!");
		return !path.equals("/restController1/method1");
	}

	@Override
	public boolean onResponse(HttpRequest request, HttpResponse response) {
		return true;
	}

}
