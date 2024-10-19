package dev.eshan.productservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RetryableHttpServiceException extends ResponseStatusException {
    public RetryableHttpServiceException(HttpStatus status) {
        super(status);
    }

    public RetryableHttpServiceException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
