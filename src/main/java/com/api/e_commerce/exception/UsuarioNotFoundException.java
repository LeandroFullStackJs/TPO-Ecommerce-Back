package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando un usuario no es encontrado
 */
public class UsuarioNotFoundException extends RuntimeException {
    
    public UsuarioNotFoundException(Long id) {
        super("No se encontró el usuario con id: " + id);
    }

    public UsuarioNotFoundException(String email) {
        super("No se encontró el usuario con email: " + email);
    }

    public UsuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}