package io.github.needle.http.protocol;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public enum HttpContentType {

	JSON("application/json"),
	TEXT("text/html"),
	XML("text/xml"),
	IMAGE_GIF("image/gif"),
	IMAGE_JPG("image/jpg"),
	IMAGE_PNG("image/png"),

	;

	HttpContentType(String httpContentType) {
		this.httpContentType = httpContentType;
	}

	private String httpContentType;

	public String getHttpContentType() {
		return httpContentType;
	}

}
