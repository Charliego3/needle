package io.github.needle.http.exception;

public class ServeException extends RuntimeException {

	public ServeException(String msg) {
		super(msg);
	}

	public ServeException(String msg, Throwable err) {
		super(msg, err);
	}

}
