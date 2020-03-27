package io.github.needle.http;

import io.github.kits.Strings;
import io.github.needle.http.exception.HandlerException;

import java.util.Arrays;

public enum HttpMethod {

	GET,
	HEAD,
	POST,
	PUT,
	PATCH,
	DELETE,
	CONNECT,
	OPTIONS,
	TRACE,

	ALL,

	;

	public static HttpMethod getInstance(String method) {
		if (Strings.isNotBlack(method)) {
			return Arrays.stream(values())
				  .filter(m -> m.name().equals(method))
				  .findFirst().orElse(HttpMethod.GET);
		}
		throw new HandlerException("The http method is invalid.");
	}
	
}
