package com.api.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO para Direccion compatible con frontend
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private Long id;
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
    private String pais;
    private String codigoPostal;
    private String observaciones;
    private Boolean esPrincipal;
    private Long usuarioId; // Para asociar con usuario
}