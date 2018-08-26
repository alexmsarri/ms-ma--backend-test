package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class CheckedException extends RuntimeException {

	private static final long serialVersionUID = 2892079610450661002L;

	private static final String CODE = "BUSINESS_ERROR";
	private static final String MSG = "General business error";
	private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

	public CheckedException() {
		super();
	}

	public String getCode() {
		return CODE;
	}

	public String getMsg() {
		return MSG;
	}

	public HttpStatus getHttpStatus() {
		return HTTP_STATUS;
	}

}
