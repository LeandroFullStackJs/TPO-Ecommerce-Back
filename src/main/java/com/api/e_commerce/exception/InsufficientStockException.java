package com.api.e_commerce.exception;

import com.api.e_commerce.dto.StockErrorDetails;

/**
 * Excepción lanzada cuando no hay suficiente stock para un producto
 */
public class InsufficientStockException extends RuntimeException {
    
    private final StockErrorDetails details;

    public InsufficientStockException(Long productoId, String producto, int stockDisponible, int stockSolicitado) {
        super(String.format("Stock insuficiente para el producto '%s'. Stock disponible: %d, Stock solicitado: %d", 
              producto, stockDisponible, stockSolicitado));
        this.details = new StockErrorDetails(productoId, producto, stockDisponible, stockSolicitado);
    }

    public InsufficientStockException(String message) {
        super(message);
        this.details = new StockErrorDetails(null, null, 0, 0);
    }

    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
        this.details = new StockErrorDetails(null, null, 0, 0);
    }

    public StockErrorDetails getDetails() {
        return details;
    }
}