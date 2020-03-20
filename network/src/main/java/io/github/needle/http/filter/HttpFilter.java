package io.github.needle.http.filter;

import io.github.needle.http.HttpRequest;
import io.github.needle.http.HttpResponse;

public interface HttpFilter {

	boolean onRequest(HttpRequest request, HttpResponse response);

	boolean onResponse(HttpRequest request, HttpResponse response);

}
