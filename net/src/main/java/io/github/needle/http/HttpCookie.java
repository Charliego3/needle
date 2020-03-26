package io.github.needle.http;

public class HttpCookie {

	private String domain;
	private String path;
	private int maxAge = -999999;
	private String expires;
	private boolean secure;
	private boolean httpOnly;

	private String name;
	private String value;

	public HttpCookie() {
	}

	public HttpCookie(String domain, String path, int maxAge, String expires, boolean secure, boolean httpOnly, String name, String value) {
		this.domain = domain;
		this.path = path;
		this.maxAge = maxAge;
		this.expires = expires;
		this.secure = secure;
		this.httpOnly = httpOnly;
		this.name = name;
		this.value = value;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
