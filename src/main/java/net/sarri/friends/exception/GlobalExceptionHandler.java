package net.sarri.friends.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@ControllerAdvice
@Slf4j
@ApiIgnore
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CheckedException.class)
	public ResponseEntity<RestError> handleCheckedException(CheckedException ex, WebRequest request) {
		log.error("Checked Exception Error", ex);
		RestError restError = new RestError(LocalDateTime.now(), ex.getCode(), ex.getMsg());
		return new ResponseEntity<>(restError, ex.getHttpStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.error("Method Not Valid Error", ex);
		BindingResult result = ex.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();
		return new ResponseEntity<>(new RestError(LocalDateTime.now(), "VALIDATION_ERROR", fieldErrors.toString()),
				status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<RestError> handleGeneralException(Exception ex, WebRequest request) {
		log.error("General Error", ex);
		return new ResponseEntity<>(new RestError(LocalDateTime.now(), "GENERAL_ERROR", "General error"),
				HttpStatus.BAD_REQUEST);
	}
}
