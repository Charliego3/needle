package io.github.needle.http;

public class MuxEntry {

	private HttpMethod method;
	private HttpHandler handler;

	public MuxEntry() {
	}

	public MuxEntry(HttpMethod method, HttpHandler handler) {
		this.method = method;
		this.handler = handler;
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
