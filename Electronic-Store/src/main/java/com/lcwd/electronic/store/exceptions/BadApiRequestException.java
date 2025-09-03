package com.lcwd.electronic.store.exceptions;

public class BadApiRequestException  extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2685314736479345439L;

	public BadApiRequestException() {
		super();
	}
	
	public BadApiRequestException(String message){
		super(message);
	}
	
	public BadApiRequestException(String message,Throwable throwable){
		super(message,throwable);
	}
}
