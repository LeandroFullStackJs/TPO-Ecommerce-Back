package com.api.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para PedidoItem - Items dentro de un pedido
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoItemDTO {
    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private Long pedidoId;
    private Long productoId;
    
    // Datos del producto para el frontend
    private String nombreObra;
    private String imagen;
    private String artista;
}