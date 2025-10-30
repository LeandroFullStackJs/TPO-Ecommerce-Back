package com.api.e_commerce.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility para generar contraseñas BCrypt
 * Ejecutar este main para obtener las contraseñas encriptadas
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== CONTRASEÑAS ENCRIPTADAS PARA SQL ===");
        System.out.println();
        
        // Contraseña para admin
        String adminPassword = "admin123";
        String adminEncrypted = encoder.encode(adminPassword);
        System.out.println("Admin:");
        System.out.println("  Email: admin@arte.com");
        System.out.println("  Password: " + adminPassword);
        System.out.println("  Encrypted: " + adminEncrypted);
        System.out.println();
        
        // Contraseña para galería
        String galeriaPassword = "galeria123";
        String galeriaEncrypted = encoder.encode(galeriaPassword);
        System.out.println("Galería:");
        System.out.println("  Email: galeria@arte.com");
        System.out.println("  Password: " + galeriaPassword);
        System.out.println("  Encrypted: " + galeriaEncrypted);
        System.out.println();
        
        System.out.println("=== SQL PARA COPIAR ===");
        System.out.println("INSERT INTO usuarios (nombre, apellido, email, password, role) VALUES");
        System.out.println("('Admin', 'Sistema', 'admin@arte.com', '" + adminEncrypted + "', 'ADMIN'),");
        System.out.println("('Galería', 'Arte Moderno', 'galeria@arte.com', '" + galeriaEncrypted + "', 'USER');");
    }
}