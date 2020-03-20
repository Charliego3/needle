package io.github.needle.http.exception;

public class HandlerException extends RuntimeException {

	public HandlerException(String msg) {
		super(msg);
	}

	public HandlerException(String msg, Throwable err) {
		super(msg, err);
	}

}
