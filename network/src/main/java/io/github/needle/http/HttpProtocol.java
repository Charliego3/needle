package io.github.needle.http;

import io.github.kits.Strings;

public class HttpProtocol {

	private HttpMethod method;

	private String version = "HTTP/1.1";

	private int versionMajor = 1;

	private int versionMinor = 1;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
		if (Strings.isNotBlack(version)) {
			String numberVersion = version.substring(version.indexOf('/') + 1);
			String[] numbers     = numberVersion.split("\\.");
			versionMajor = Integer.parseInt(numbers[0]);
			if (numbers.length == 2) {
				versionMinor = Integer.parseInt(numbers[1]);
			} else {
				versionMinor = 0;
			}
		}
	}

	public int getVersionMajor() {
		return versionMajor;
	}

	public int getVersionMinor() {
		return versionMinor;
	}

}
