package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando hay un conflicto con datos duplicados
 */
public class DuplicateDataException extends RuntimeException {
    
    public DuplicateDataException(String resource, String field, String value) {
        super(String.format("Ya existe un %s con %s: %s", resource, field, value));
    }

    public DuplicateDataException(String message) {
        super(message);
    }

    public DuplicateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}