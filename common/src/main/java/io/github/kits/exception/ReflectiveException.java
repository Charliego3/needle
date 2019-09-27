package io.github.kits.exception;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class ReflectiveException extends RuntimeException {

	public ReflectiveException(String message) {
		super(message);
	}

	public ReflectiveException(Throwable throwable) {
		super(throwable);
	}

}
