package com.auction.exception;

public class AuctionException extends Exception {

	private static final long serialVersionUID = -330364514829795317L;

	private Integer statusCode;
	private String message;


	public AuctionException(String message, Integer statusCode, Throwable cause) {
		super(message, cause);
		this.message = message;
		this.statusCode = statusCode;
	}

	public AuctionException(String message, Integer statusCode) {
		super(message);
		this.message = message;
		this.statusCode = statusCode;
	}
	
	public AuctionException(String message) {
		super(message);
		this.message = message;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
