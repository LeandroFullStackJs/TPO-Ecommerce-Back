package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando hay problemas de autenticación o autorización
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}