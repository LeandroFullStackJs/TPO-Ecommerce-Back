package com.api.e_commerce.dto;

/**
 * DTO para proporcionar detalles estructurados sobre errores de stock insuficiente.
 * Permite al cliente entender el contexto del error sin parsear mensajes.
 */
public class StockErrorDetails {

    private Long productoId;
    private String productoNombre;
    private int stockDisponible;
    private int stockSolicitado;

    public StockErrorDetails(Long productoId, String productoNombre, int stockDisponible, int stockSolicitado) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stockDisponible = stockDisponible;
        this.stockSolicitado = stockSolicitado;
    }

    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public String getProductoNombre() { return productoNombre; }
    public int getStockDisponible() { return stockDisponible; }
    public int getStockSolicitado() { return stockSolicitado; }
}