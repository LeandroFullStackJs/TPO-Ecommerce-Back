package com.api.e_commerce.exception;

public class ProductoNotFoundException extends RuntimeException {
    
    public ProductoNotFoundException(Long id) {
        super("No se encontró el producto con id: " + id);
    }

    // Constructor: Acepta cualquier String como mensaje.
    public ProductoNotFoundException(String message) {
        super(message);
    }    
}
