package com.api.e_commerce.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PedidoDTO {
    private Long id;
    private LocalDateTime fecha;
    private String estado;
    private Long usuarioId;
    private String usuarioNombre;
}