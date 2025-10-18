package com.api.e_commerce.dto;

import lombok.Data;

@Data
public class DireccionDTO {
    private Long id;
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
    private String pais;
}