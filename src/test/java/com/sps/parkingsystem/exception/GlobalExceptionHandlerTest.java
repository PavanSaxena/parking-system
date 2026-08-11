package com.sps.parkingsystem.exception;

import com.sps.parkingsystem.dto.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesResourceNotFoundAndInvalidCredentials() {
        ResponseEntity<ErrorResponse> notFound = handler.handleNotFound(new ResourceNotFoundException("missing"));
        ResponseEntity<ErrorResponse> invalid = handler.handleInvalidCredentials(new InvalidCredentialsException("bad"));

        assertEquals(HttpStatus.NOT_FOUND, notFound.getStatusCode());
        assertEquals("missing", notFound.getBody().getError());
        assertEquals(HttpStatus.UNAUTHORIZED, invalid.getStatusCode());
        assertEquals("bad", invalid.getBody().getError());
    }

    @Test
    void handlesBusinessAndUnhandledErrors() {
        ResponseEntity<ErrorResponse> business = handler.handleBusinessErrors(new PaymentProcessingException("pay"));
        ResponseEntity<ErrorResponse> unhandled = handler.handleUnhandled(new RuntimeException("boom"));

        assertEquals(HttpStatus.BAD_REQUEST, business.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, unhandled.getStatusCode());
        assertEquals("Unexpected server error", unhandled.getBody().getError());
    }
}
