package com.myretail.catalog.product.ws.exception;

public class ProductServiceException extends RuntimeException {
	
	
	private static final long serialVersionUID = 5193769073019856771L;

	private final String errorCode;

	private final String developerMessage;
	
	
	public ProductServiceException(final String errorCode, final String message) {
		super(buildExceptionMessage(errorCode, message));
		this.errorCode = errorCode;
		this.developerMessage = message;
	}

	private static String buildExceptionMessage(final String errorCode, final String message) {
		return "ERROR CODE =" + errorCode + ", ERROR MESSAGE = " + message;
	}
	
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

}
