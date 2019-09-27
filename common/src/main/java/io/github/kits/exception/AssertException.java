package io.github.kits.exception;

/**
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class AssertException extends RuntimeException {

	public AssertException() {
		super();
	}

	public AssertException(Throwable throwable) {
		super(throwable);
	}

	public AssertException(String message) {
		super(message);
	}

}
