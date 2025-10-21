package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando un artista no es encontrado
 */
public class ArtistaNotFoundException extends RuntimeException {
    
    public ArtistaNotFoundException(Long id) {
        super("No se encontró el artista con id: " + id);
    }

    public ArtistaNotFoundException(String email) {
        super("No se encontró el artista con email: " + email);
    }

    public ArtistaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}