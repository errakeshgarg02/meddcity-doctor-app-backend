/*
 * arg license
 *
 */

package com.arg.auth.exception;

import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.arg.auth.dto.response.ErrorResponse;


@ControllerAdvice
public class AuthServerExceptionHandler {

    @ExceptionHandler(value = {BadRequestException.class,
            DecodingException.class})
    public ResponseEntity<ErrorResponse> badRequest(
            BadRequestException exception) {
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                exception.getMessage());
    }

    @ExceptionHandler(value = {InvalidGrantException.class})
    public ResponseEntity<ErrorResponse> invalidGrant(
            InvalidGrantException exception) {
        return prepareErrorResponse(HttpStatus.BAD_REQUEST,
                exception.getMessage());
    }

    @ExceptionHandler(value = {ArgException.class})
    public ResponseEntity<ErrorResponse> crmException(
            ArgException exception) {
        return prepareErrorResponse(exception.getStatus(),
                exception.getMessage());
    }


    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ErrorResponse> internalServerError(
            Throwable exception) {
        return prepareErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
    }


    private ResponseEntity<ErrorResponse> prepareErrorResponse(
            HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponse.builder().errorCode(httpStatus.value())
                        .errorDesc(httpStatus.getReasonPhrase())
                        .userDesc(message).build());
    }
}
