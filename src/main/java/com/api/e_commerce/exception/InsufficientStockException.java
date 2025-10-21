package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando no hay suficiente stock para un producto
 */
public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String producto, int stockDisponible, int stockSolicitado) {
        super(String.format("Stock insuficiente para el producto '%s'. Stock disponible: %d, Stock solicitado: %d", 
              producto, stockDisponible, stockSolicitado));
    }

    public InsufficientStockException(String message) {
        super(message);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}