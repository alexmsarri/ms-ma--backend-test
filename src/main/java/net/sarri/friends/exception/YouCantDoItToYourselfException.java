package net.sarri.friends.exception;

import org.springframework.http.HttpStatus;

public class YouCantDoItToYourselfException extends CheckedException {

	private static final long serialVersionUID = -680782283715693158L;
	
	private static final String CODE = "ITS_YOU";
	private static final String MSG = "You can't do it to yourself";
	private static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;

	public YouCantDoItToYourselfException() {
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
