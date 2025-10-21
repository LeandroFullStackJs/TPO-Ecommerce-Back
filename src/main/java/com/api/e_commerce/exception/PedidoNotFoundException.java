package com.api.e_commerce.exception;

/**
 * Excepción lanzada cuando un pedido no es encontrado
 */
public class PedidoNotFoundException extends RuntimeException {
    
    public PedidoNotFoundException(Long id) {
        super("No se encontró el pedido con id: " + id);
    }

    public PedidoNotFoundException(String message) {
        super(message);
    }

    public PedidoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}