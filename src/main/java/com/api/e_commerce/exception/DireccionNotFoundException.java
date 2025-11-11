package com.api.e_commerce.exception;

public class DireccionNotFoundException extends RuntimeException {
    
    public DireccionNotFoundException(Long id) {
        super("No se encontró la dirección con id: " + id);
    }

    public DireccionNotFoundException(String message) {
        super(message);
    }

    public DireccionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
