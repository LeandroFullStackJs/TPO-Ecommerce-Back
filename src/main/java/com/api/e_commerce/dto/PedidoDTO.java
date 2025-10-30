package com.api.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para Pedido optimizado con items y direcciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private Double total;
    private String notas;
    private LocalDateTime fechaActualizacion;
    
    // Información del usuario
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    
    // Items del pedido
    private List<PedidoItemDTO> items = new ArrayList<>();
    
    // Dirección de envío
    private Long direccionEnvioId;
    private DireccionDTO direccionEnvio;
}