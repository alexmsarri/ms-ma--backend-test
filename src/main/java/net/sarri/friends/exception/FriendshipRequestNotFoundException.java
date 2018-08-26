package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class FriendshipRequestNotFoundException extends CheckedException {

	private static final long serialVersionUID = 8205013212595074454L;
	
	private static final String CODE = "REQUEST_NOT_FOUND";
	private static final String MSG = "The friendship request doesn't exists";
	private static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;

	public FriendshipRequestNotFoundException() {
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
