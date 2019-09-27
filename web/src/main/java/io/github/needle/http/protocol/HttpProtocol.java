package io.github.needle.http.protocol;

import io.github.kits.json.annotations.IgnoreJsonSerialize;
import io.github.needle.http.enums.HttpMethod;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpProtocol {

	@IgnoreJsonSerialize
	protected static final String VERSION_1_1 = "HTTP/1.1";

	/**
	 * 方法名称 GET / POST...
	 */
	private HttpMethod method;

	/**
	 * 版本号 HTTP/1.1
	 */
	private String version;

	/**
	 * 请求参数
	 */
	private String queryString;

	public HttpProtocol() {
		this.version = VERSION_1_1;
		this.method = HttpMethod.GET;
	}

	public HttpProtocol(HttpMethod method, String version) {
		this.method = method;
		this.version = version;
	}

	public HttpProtocol(HttpMethod method, String version, String queryString) {
		this.method = method;
		this.version = version;
		this.queryString = queryString;
	}

	public static HttpProtocol get() {
		return new HttpProtocol(HttpMethod.GET, VERSION_1_1);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public HttpProtocol setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}

	public String getVersion() {
		return version;
	}

	public HttpProtocol setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

}
