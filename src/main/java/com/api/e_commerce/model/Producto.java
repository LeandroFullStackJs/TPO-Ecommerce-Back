package com.api.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la obra es obligatorio")
    @Column(nullable = false)
    private String nombreObra;

    @Column(nullable = false, length = 1000)
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false)
    private Double precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;
    
    // Campos adicionales para el frontend
    @Column(nullable = false)
    private String imagen; // URL de la imagen principal
      
    private Boolean activo = true;
    
    private Boolean destacado = false;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    // Campos específicos para obras de arte (exactos del frontend)

    private String artista; // Nombre del artista (para búsquedas rápidas)
    
    // Relación con la entidad Artista
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artista_id", foreignKey = @ForeignKey(name = "FK_producto_artista"))
    private Artista artistaEntity;
    
    // Relación con Usuario que creó el producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "FK_producto_usuario"))
    private Usuario usuarioCreador;
    
    @Column(nullable = false)
    private String tecnica; // technique - Óleo, acuarela, acrílico, etc.
    
    @Column(nullable = false)
    private String dimensiones; // dimensions - "80x60 cm"
    @Column(nullable = false)
    private Integer anio; // year - Año de creación
    @Column(nullable = false)
    private String estilo; // style - Estilo artístico (opcional)
    
    // Relación Many-to-Many con Categorías con configuración optimizada
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "productos_categorias",
        joinColumns = @JoinColumn(name = "producto_id", foreignKey = @ForeignKey(name = "FK_productos_categorias_producto")),
        inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "FK_productos_categorias_categoria"))
    )
    private List<Categoria> categorias = new ArrayList<>();
    
    // Relación con elementos de pedidos
    @JsonIgnore
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PedidoItem> pedidoItems = new ArrayList<>();
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}
