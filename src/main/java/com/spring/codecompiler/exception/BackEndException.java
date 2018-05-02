package com.spring.codecompiler.exception;

public class BackEndException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackEndException(String message) {
		super(message);
	}

	public BackEndException(String message, Throwable cause) {
		super(message, cause);
	}


}
