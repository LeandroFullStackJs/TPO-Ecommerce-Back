package com.api.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa una categoría de productos/obras de arte
 * Optimizada con relaciones y validaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Column(nullable = false, unique = true)    
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(name = "imagen_icono")
    private String imagenIcono; // URL del icono de la categoría
    
    @Column(name = "activa")
    private Boolean activa = true;
    
    @Column(name = "orden")
    private Integer orden = 0; // Para ordenar categorías en el frontend
    
    // Relación Many-to-Many con Productos (lado inverso)
    @JsonIgnore
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();
    
    /**
     * Constructor básico con nombre
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Constructor con nombre y descripción
     */
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    /**
     * Cuenta la cantidad de productos activos en esta categoría
     */
    public long contarProductosActivos() {
        return productos.stream()
            .filter(p -> p.getActivo() != null && p.getActivo())
            .count();
    }
}
