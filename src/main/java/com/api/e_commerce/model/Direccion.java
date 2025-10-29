package com.api.e_commerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "direcciones")
public class Direccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String calle;
    @Column(nullable = false)
    private String numero;
    @Column(nullable = false)
    private String localidad;
    @Column(nullable = false)
    private String provincia;
    @Column(nullable = false)
    private String pais;

    public Direccion(Long id, String calle, String numero, String localidad, String provincia, String pais) {
        this.id = id;
        this.calle = calle;
        this.numero = numero;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
    }
    
}