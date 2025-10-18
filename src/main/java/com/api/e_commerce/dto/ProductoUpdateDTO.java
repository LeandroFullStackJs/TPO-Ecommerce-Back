package com.api.e_commerce.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoUpdateDTO {
    private String nombre;
    private String descripcion;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private Double precio;
    
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    private String imagen;
    private String imagenAdicional1;
    private String imagenAdicional2;
    private String imagenAdicional3;
    private Boolean activo;
    private Boolean destacado;
    // Campos específicos para obras de arte (exactos del frontend)
    private String artista; // Nombre del artista
    private Long artistaId; // ID del artista
    private Long usuarioId; // ID del usuario que creó el producto
    private String tecnica; // technique - Óleo, acuarela, acrílico, etc.
    private String dimensiones; // dimensions - "80x60 cm"
    private Integer anio; // year - Año de creación
    private String estilo; // style - Estilo artístico (opcional)
    private List<Long> categoriaIds;
}
