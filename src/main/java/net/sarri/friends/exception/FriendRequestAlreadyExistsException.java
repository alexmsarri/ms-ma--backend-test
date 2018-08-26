package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class FriendRequestAlreadyExistsException extends CheckedException {

	private static final long serialVersionUID = 5739839882117729022L;
	
	private static final String CODE = "FRIEND_REQUEST_ALREADY_EXISTS";
	private static final String MSG = "A friendship with this user is already requested";
	private static final HttpStatus HTTP_STATUS = HttpStatus.CONFLICT;

	public FriendRequestAlreadyExistsException() {
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
