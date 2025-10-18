package com.api.e_commerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String role;
    
    public AuthResponse(String token, Long id, String email, String nombre, String apellido, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellido = apellido;
        this.role = role;
    }
}