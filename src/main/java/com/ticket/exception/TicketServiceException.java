package com.ticket.exception;

public class TicketServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorCode = "Unknown_Exception";
	
	public TicketServiceException () {

	}
	
	public TicketServiceException (String erroMessage) {
		super(erroMessage);
	}
	
	public TicketServiceException (String erroMessage, String errorCode) {
		super(erroMessage);
		this.errorCode = errorCode;
	}
	
	public TicketServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TicketServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


}
