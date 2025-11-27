package com.code.product_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejo de 404 (Recurso no encontrado)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }

    // Manejo de 500 (Errores inesperados)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    // Método auxiliar para generar la estructura JSON API
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String title, String detail) {
        // Estructura manual JSON: { "errors": [ { ... } ] }
        String jsonError = String.format(
            "{\"errors\": [{\"status\": \"%d\", \"title\": \"%s\", \"detail\": \"%s\"}]}",
            status.value(), title, detail
        );
        return ResponseEntity.status(status)
                .header("Content-Type", "application/vnd.api+json")
                .body(jsonError);
    }
}
