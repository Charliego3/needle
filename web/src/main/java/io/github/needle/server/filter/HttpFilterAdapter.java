package io.github.needle.server.filter;

import io.github.kits.Envs;
import io.github.kits.Lists;
import io.github.kits.log.Logger;
import io.github.needle.constants.Consts;
import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpFilterAdapter {

	private              HttpRequest     request;
	private              HttpResponse    response;
	private static final Set<HttpFilter> filterList = new TreeSet<>(Consts.orderComparator());

	public HttpFilterAdapter() {}

	public HttpFilterAdapter(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
	}

	public void registerFilter(String classpath) throws IOException, IllegalAccessException, InstantiationException {
		List<Class> filters = Envs.getDirClass(new File(classpath), HttpFilter.class);
		if (Lists.isNotNullOrEmpty(filters)) {
			for (Class filter : filters) {
				filterList.add((HttpFilter) filter.newInstance());
				Logger.infof("Register Filter: {}", filter.getCanonicalName());
			}
		}
	}

	public boolean onRequest() {
		for (HttpFilter filter : filterList) {
			if (!filter.onRequest(request, response)) {
				return false;
			}
		}
		return true;
	}

	public boolean onResponse() {
		for (HttpFilter filter : filterList) {
			if (!filter.onResponse(request, response)) {
				return false;
			}
		}
		return true;
	}

}
