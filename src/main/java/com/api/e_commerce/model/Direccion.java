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
 * Entidad que representa una dirección física
 * Optimizada con relaciones y validaciones
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direcciones")
public class Direccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La calle es obligatoria")
    @Column(nullable = false)
    private String calle;
    
    @NotBlank(message = "El número es obligatorio")
    @Column(nullable = false)
    private String numero;
    
    @NotBlank(message = "La localidad es obligatoria")
    @Column(nullable = false)
    private String localidad;
    
    @NotBlank(message = "La provincia es obligatoria")
    @Column(nullable = false)
    private String provincia;
    
    @NotBlank(message = "El país es obligatorio")
    @Column(nullable = false)
    private String pais;
    
    @Column(name = "codigo_postal")
    private String codigoPostal;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "es_principal")
    private Boolean esPrincipal = false;
    
    // Relación con Usuario propietario de la dirección
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "FK_direccion_usuario"))
    private Usuario usuario;
    
    // Relación con pedidos que usan esta dirección como envío
    @JsonIgnore
    @OneToMany(mappedBy = "direccionEnvio", fetch = FetchType.LAZY)
    private List<Pedido> pedidosEnvio = new ArrayList<>();
    
    /**
     * Constructor para crear dirección con parámetros básicos
     */
    public Direccion(String calle, String numero, String localidad, String provincia, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
    }
    
    /**
     * Retorna la dirección completa como String
     */
    public String getDireccionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(calle).append(" ").append(numero);
        if (codigoPostal != null && !codigoPostal.isEmpty()) {
            sb.append(", CP: ").append(codigoPostal);
        }
        sb.append(", ").append(localidad)
          .append(", ").append(provincia)
          .append(", ").append(pais);
        return sb.toString();
    }
    
}