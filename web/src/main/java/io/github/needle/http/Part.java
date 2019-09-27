package io.github.needle.http;

import java.io.File;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Part {

	private String name;
	private long size;
	private File body;

	public Part(File part) {
		this.name = part.getName();
		this.size = part.length();
		this.body = part;
	}

	public String getName() {
		return name;
	}

	public Part setName(String name) {
		this.name = name;
		return this;
	}

	public long getSize() {
		return size;
	}

	public Part setSize(long size) {
		this.size = size;
		return this;
	}

	public File getBody() {
		return body;
	}

	public Part setBody(File body) {
		this.body = body;
		return this;
	}

}
