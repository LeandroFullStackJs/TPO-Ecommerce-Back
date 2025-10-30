package com.api.e_commerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "artistas")
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre del artista es obligatorio")
    @Column(nullable = false)
    private String nombre; // name en frontend
    
    @NotBlank(message = "La biografía es obligatoria")
    @Column(nullable = false, length = 2000)
    private String biografia; // biography en frontend

    private String imagenPerfil; // profileImage en frontend
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email debe ser válido")
    @Column(nullable = false)
    private String email; // email de contacto
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
    
    // Relación con productos - Un artista puede tener muchas obras
    @JsonIgnore
    @OneToMany(mappedBy = "artistaEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Producto> productos = new ArrayList<>();
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}