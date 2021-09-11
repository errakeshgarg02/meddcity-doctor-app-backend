/*
 * arg license
 *
 */

package com.arg.auth.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class ArgException extends RuntimeException {

    private static final long serialVersionUID = 6006990354909042677L;

    @Getter
    @Setter
    private HttpStatus status;

    public ArgException() {
        super();
    }

    public ArgException(String message) {
        super(message);
    }

    public ArgException(String message, Throwable error) {
        super(message, error);
    }

    public ArgException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
