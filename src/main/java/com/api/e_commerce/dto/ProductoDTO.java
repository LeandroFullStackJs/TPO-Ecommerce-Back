package com.api.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Long id;
    private String nombreObra;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String imagen;
    private Boolean activo;
    private Boolean destacado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    // Campos específicos para obras de arte (exactos del frontend)
    private String artista; // Nombre del artista
    private Long artistaId; // ID del artista
    private Long usuarioId; // ID del usuario que creó el producto
    private String tecnica; // technique - Óleo, acuarela, acrílico, etc.
    private String dimensiones; // dimensions - "80x60 cm"
    private Integer anio; // year - Año de creación
    private String estilo; // style - Estilo artístico (opcional)
    private List<String> categorias; // Solo nombres de categorías para el frontend
}