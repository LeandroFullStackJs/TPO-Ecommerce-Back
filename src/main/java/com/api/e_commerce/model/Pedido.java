package com.api.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un pedido de productos
 * Optimizada con relaciones completas y mejores prácticas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;
    
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo")
    @Column(name = "total")
    private Double total = 0.0;
    
    @Column(name = "notas")
    private String notas;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    // Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_usuario"))
    private Usuario usuario;
    
    // Relación con Items del pedido
    @OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> items = new ArrayList<>();
    
    // Relación con Dirección de envío
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direccion_envio_id", foreignKey = @ForeignKey(name = "FK_pedido_direccion"))
    private Direccion direccionEnvio;
    
    /**
     * Enum para estados del pedido
     */
    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_PROCESO,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }
    
    /**
     * Calcula el total del pedido basado en los items
     */
    public void calcularTotal() {
        this.total = items.stream()
            .mapToDouble(item -> item.getSubtotal())
            .sum();
    }
    
    /**
     * Agrega un item al pedido
     */
    public void agregarItem(PedidoItem item) {
        items.add(item);
        item.setPedido(this);
        calcularTotal();
    }
    
    /**
     * Remueve un item del pedido
     */
    public void removerItem(PedidoItem item) {
        items.remove(item);
        item.setPedido(null);
        calcularTotal();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        calcularTotal();
    }
    
    @PrePersist
    public void prePersist() {
        calcularTotal();
    }
    
}
