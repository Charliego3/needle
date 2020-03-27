package io.github.needle.http;

public class MuxEntry {

	private HttpMethod method;
	private HttpHandler handler;
	private String pattern;

	public MuxEntry() {
	}

	public MuxEntry(HttpMethod method, String pattern, HttpHandler handler) {
		this.method = method;
		this.pattern = pattern;
		this.handler = handler;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public HttpHandler getHandler() {
		return handler;
	}

	public void setHandler(HttpHandler handler) {
		this.handler = handler;
	}

}
