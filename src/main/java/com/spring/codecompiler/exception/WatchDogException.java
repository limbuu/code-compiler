package com.spring.codecompiler.exception;

public class WatchDogException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WatchDogException(String message) {
		super(message);
	}

	public WatchDogException(String message, Throwable cause) {
		super(message, cause);
	}

}
