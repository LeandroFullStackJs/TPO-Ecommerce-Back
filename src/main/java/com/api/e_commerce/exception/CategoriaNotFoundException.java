package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando una categoría no es encontrada
 */
public class CategoriaNotFoundException extends RuntimeException {
    
    public CategoriaNotFoundException(Long id) {
        super("No se encontró la categoría con id: " + id);
    }

    public CategoriaNotFoundException(String nombre) {
        super("No se encontró la categoría con nombre: " + nombre);
    }

    public CategoriaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}