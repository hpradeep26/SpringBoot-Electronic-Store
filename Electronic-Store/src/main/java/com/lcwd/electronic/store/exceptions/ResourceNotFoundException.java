package com.lcwd.electronic.store.exceptions;

public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String message){
		super(message);
	}
	
	public ResourceNotFoundException(String message,Throwable throwable){
		super(message,throwable);
	}
}
