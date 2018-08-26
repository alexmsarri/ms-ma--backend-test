package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class YouAreAlreadyFriendsException extends CheckedException {

	private static final long serialVersionUID = 8295481434438532695L;
	
	private static final String CODE = "ALREADY_FRIENDS";
	private static final String MSG = "You are already friends";
	private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

	public YouAreAlreadyFriendsException() {
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
