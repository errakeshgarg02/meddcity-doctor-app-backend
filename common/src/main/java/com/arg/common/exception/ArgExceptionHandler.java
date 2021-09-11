/*
 * arg license
 *
 */

package com.arg.common.exception;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice
public class ArgExceptionHandler {

	@ExceptionHandler(value = { BadRequestException.class, DecodingException.class })
	public ResponseEntity<ErrorResponse> badRequest(BadRequestException exception) {
		return prepareErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler(value = { AccessDeniedException.class })
	public ResponseEntity<ErrorResponse> accessDenied(AccessDeniedException exception) {
		return prepareErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
	}

	@ExceptionHandler(value = { WebExchangeBindException.class })
	public ResponseEntity<ErrorResponse> validationError(WebExchangeBindException exception) {
		BindingResult bindingResult = exception.getBindingResult();
		List<String> collect = Optional.ofNullable(bindingResult).map(bindingR -> bindingR.getFieldErrors()).map(
				list -> list.stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList()))
				.orElse(Arrays.asList(exception.getMessage()));
		return prepareErrorResponse(exception.getStatus(), collect.toString());
	}

	@ExceptionHandler(value = { Throwable.class })
	public ResponseEntity<ErrorResponse> internalServerError(Throwable exception) {
		return prepareErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

	@ExceptionHandler(value = { ArgException.class })
	public ResponseEntity<ErrorResponse> crmException(ArgException exception) {
		return prepareErrorResponse(exception.getStatus(), exception.getMessage());
	}

	@ExceptionHandler(value = { SyncClientException.class })
	public ResponseEntity<ErrorResponse> syncClientException(SyncClientException exception) {
		return prepareErrorResponse(exception.getStatus(), exception.getMessage());
	}

	private ResponseEntity<ErrorResponse> prepareErrorResponse(HttpStatus httpStatus, String message) {
		return ResponseEntity.status(httpStatus).body(ErrorResponse.builder().errorCode(httpStatus.value())
				.errorDesc(httpStatus.getReasonPhrase()).userDesc(message).build());
	}
}
