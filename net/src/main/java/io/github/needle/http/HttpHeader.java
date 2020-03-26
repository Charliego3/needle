package io.github.needle.http;

import io.github.kits.Lists;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeader {

	private final Map<String, List<String>> headers;

	public HttpHeader() {
		this.headers = new HashMap<>();
	}

	public void add(String key, String value) {
		if (headers.containsKey(key)) {
			List<String> values = headers.get(key);
			if (Lists.isNullOrEmpty(values)) {
				values = new ArrayList<>();
			}
			values.add(value);
		} else {
			this.headers.put(key, Lists.asList(value));
		}
	}

	public void set(String key, String value) {
		this.headers.put(key, Lists.asList(value));
	}

	public String get(String key) {
		if (this.headers.containsKey(key)) {
			List<String> values = this.headers.get(key);
			if (Lists.isNotNullOrEmpty(values)) {
				return values.get(0);
			}
		}
		return null;
	}

	public boolean has(String key) {
		if (this.headers.containsKey(key)) {
			return Lists.isNotNullOrEmpty(this.headers.get(key));
		}
		return false;
	}

	public void write(Writer writer) throws IOException {
		writer.write("c");
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

}
