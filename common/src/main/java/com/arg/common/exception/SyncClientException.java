/*
 * arg license
 *
 */

package com.arg.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

public class SyncClientException extends RuntimeException {

    private static final long serialVersionUID = -2560391064369170936L;

    @Getter
    @Setter
    private HttpStatus status;

    public SyncClientException() {
        super();
    }

    public SyncClientException(String message) {
        super(message);
    }

    public SyncClientException(String message, Throwable error) {
        super(message, error);
    }

    public SyncClientException(String message, Throwable error, HttpStatus status) {
        super(message, error);
        this.status = status;
    }

    public SyncClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
