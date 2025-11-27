package com.code.inventory_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Argumentos Inválidos (400) - Ej: Stock negativo
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 2. NUEVO: Manejo de 404 (Recurso no encontrado)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage());
    }
    
    // 3. Manejo Genérico (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", ex.getMessage());
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String title, String detail) {
        String jsonError = String.format(
            "{\"errors\": [{\"status\": \"%d\", \"title\": \"%s\", \"detail\": \"%s\"}]}",
            status.value(), title, detail
        );
        return ResponseEntity.status(status)
                .header("Content-Type", "application/vnd.api+json")
                .body(jsonError);
    }
}