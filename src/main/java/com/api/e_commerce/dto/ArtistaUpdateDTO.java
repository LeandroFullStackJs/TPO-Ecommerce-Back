package com.api.e_commerce.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaUpdateDTO {
    private String nombre; // name
    private String biografia; // biography
    private String imagenPerfil; // profileImage
    
    @Email(message = "Email debe ser válido")
    private String email;
    
    private Boolean activo;
}