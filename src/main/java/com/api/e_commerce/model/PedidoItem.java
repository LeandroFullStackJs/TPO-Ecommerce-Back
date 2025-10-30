package com.api.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad que representa un item dentro de un pedido
 * Relación intermedia entre Pedido y Producto con cantidad y precio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_items")
public class PedidoItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El subtotal debe ser mayor a 0")
    @Column(nullable = false)
    private Double subtotal;
    
    // Relación con Pedido
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_item_pedido"))
    private Pedido pedido;
    
    // Relación con Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_item_producto"))
    private Producto producto;
    
    /**
     * Calcula el subtotal basado en cantidad y precio unitario
     */
    @PrePersist
    @PreUpdate
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad * precioUnitario;
        }
    }
}