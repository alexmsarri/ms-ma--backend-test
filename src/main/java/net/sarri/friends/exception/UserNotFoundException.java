package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CheckedException {

	private static final long serialVersionUID = 1083444414016187981L;
	
	private static final String CODE = "USER_NOT_FOUND";
	private static final String MSG = "User not found";
	private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

	public UserNotFoundException() {
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
