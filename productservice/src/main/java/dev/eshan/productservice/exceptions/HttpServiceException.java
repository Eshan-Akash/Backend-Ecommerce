package dev.eshan.productservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpServiceException extends ResponseStatusException {
    public HttpServiceException(HttpStatus status) {
        super(status);
    }

    public HttpServiceException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
