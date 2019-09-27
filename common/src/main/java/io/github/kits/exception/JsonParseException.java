package io.github.kits.exception;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class JsonParseException extends RuntimeException {

	public JsonParseException(String message) {
		super(message);
	}

	public JsonParseException(Throwable throwable) {
		super(throwable);
	}

}
