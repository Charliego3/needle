package io.github.needle.http.enums;

import io.github.kits.Strings;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum HttpMethod {

	GET, POST, PUT, HEAD, DELETE, OPTIONS, TRACE, CONNECT,

	;

	public static HttpMethod getByString(String method) {
		HttpMethod httpMethod = GET;
		if (Strings.isNotNullOrEmpty(method)) {
			if (method.equals(POST.name())) {
				httpMethod = POST;
			} else if (method.equals(PUT.name())) {
				httpMethod = PUT;
			} else if (method.equals(HEAD.name())) {
				httpMethod = HEAD;
			} else if (method.equals(DELETE.name())) {
				httpMethod = DELETE;
			} else if (method.equals(OPTIONS.name())) {
				httpMethod = OPTIONS;
			} else if (method.equals(TRACE.name())) {
				httpMethod = TRACE;
			} else if (method.equals(CONNECT.name())) {
				httpMethod = CONNECT;
			}
		}
		return httpMethod;
	}

}
