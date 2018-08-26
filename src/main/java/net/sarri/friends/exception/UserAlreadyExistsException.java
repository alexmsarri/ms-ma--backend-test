package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends CheckedException {

	private static final long serialVersionUID = -1040891208472765575L;
	
	private static final String CODE = "BUSINESS_ERROR";
	private static final String MSG = "General business error";
	private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

	public UserAlreadyExistsException() {
		super();
	}

	@Override
	public String getCode() {
		return CODE;
	}

	@Override
	public String getMsg() {
		return MSG;
	}

	@Override
	public HttpStatus getHttpStatus() {
		return HTTP_STATUS;
	}

}
