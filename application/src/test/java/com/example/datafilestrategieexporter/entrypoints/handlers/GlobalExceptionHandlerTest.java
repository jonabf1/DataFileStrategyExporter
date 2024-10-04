
package com.example.datafilestrategieexporter.entrypoints.handlers;

import com.example.datafilestrategieexporter.entrypoints.controller.handlers.ErrorResponse;
import com.example.datafilestrategieexporter.entrypoints.controller.handlers.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest request;

    @BeforeEach
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        request = mock(WebRequest.class);
    }

    @Test
    public void testHandleIllegalArgumentException_ShouldReturnBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Argumento inválido", response.getBody().getMessage());
    }

    @Test
    public void testHandleGeneralException_ShouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneralException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro inesperado", response.getBody().getMessage());
    }
}
