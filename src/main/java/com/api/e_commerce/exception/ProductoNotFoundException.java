package com.api.e_commerce.exception;

import java.util.List;
import java.util.Collections;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("No se encontró el producto con id: " + id);
        this.details = Collections.emptyList();
    }

    // Constructor: Acepta cualquier String como mensaje.
    public ProductoNotFoundException(String message) {
        super(message);
        this.details = Collections.emptyList();
    }    
    private final List<String> details;
    public ProductoNotFoundException(String message, List<String> details) {
        super(message);
        this.details = details;
    }
    public List<String> getDetails() {
        return details;
    }
}

