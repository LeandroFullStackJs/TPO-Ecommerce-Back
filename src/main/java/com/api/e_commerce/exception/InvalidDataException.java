package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando los datos de entrada no son válidos
 */
public class InvalidDataException extends RuntimeException {
    
    public InvalidDataException(String field, String value, String reason) {
        super(String.format("Datos inválidos para el campo '%s' con valor '%s': %s", field, value, reason));
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}