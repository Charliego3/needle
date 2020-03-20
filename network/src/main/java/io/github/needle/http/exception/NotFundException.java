package io.github.needle.http.exception;

public class NotFundException extends RuntimeException {

	public NotFundException(String message) {
		super(message);
	}

	public NotFundException(String message, Throwable cause) {
		super(message, cause);
	}

}
