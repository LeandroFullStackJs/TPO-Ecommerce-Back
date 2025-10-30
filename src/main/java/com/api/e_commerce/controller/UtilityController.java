package com.api.e_commerce.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Controlador con endpoints de utilidad para el frontend
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class UtilityController {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "E-Commerce Arte API");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }

    /**
     * CORS test endpoint
     */
    @GetMapping("/cors-test")
    public ResponseEntity<Map<String, Object>> corsTest(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "CORS está funcionando correctamente");
        response.put("timestamp", new Date());
        response.put("origin", request.getHeader("Origin"));
        response.put("host", request.getHeader("Host"));
        response.put("userAgent", request.getHeader("User-Agent"));
        response.put("method", request.getMethod());
        return ResponseEntity.ok(response);
    }

    /**
     * API info endpoint
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "E-Commerce Arte API");
        response.put("description", "API REST para plataforma de e-commerce de arte");
        response.put("version", "1.0.0");
        response.put("endpoints", Map.of(
            "productos", "/api/productos",
            "categorias", "/api/categorias", 
            "artistas", "/api/artistas",
            "auth", "/api/auth",
            "pedidos", "/api/pedidos"
        ));
        response.put("features", Map.of(
            "cors", true,
            "jwt", true,
            "imageProxy", true,
            "pagination", true
        ));
        return ResponseEntity.ok(response);
    }

    /**
     * Proxy para imágenes externas (evita CORB)
     */
    @GetMapping("/proxy/image")
    public ResponseEntity<byte[]> proxyImage(@RequestParam String url) {
        try {
            // Validar URL
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return ResponseEntity.badRequest().build();
            }

            // Obtener la imagen
            byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
            
            if (imageBytes == null) {
                return ResponseEntity.notFound().build();
            }

            // Headers para CORS y caching
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Asumir JPEG por defecto
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Cache-Control", "public, max-age=3600"); // Cache por 1 hora
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);
                    
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para verificar conectividad con servicios externos
     */
    @GetMapping("/connectivity/check")
    public ResponseEntity<Map<String, Object>> checkConnectivity() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        
        // Test de conectividad con Unsplash (servicio de imágenes común)
        try {
            String testUrl = "https://api.unsplash.com/photos?client_id=demo&per_page=1";
            ResponseEntity<String> unsplashResponse = restTemplate.getForEntity(testUrl, String.class);
            response.put("unsplash", Map.of(
                "status", "UP",
                "responseCode", unsplashResponse.getStatusCode()
            ));
        } catch (Exception e) {
            response.put("unsplash", Map.of(
                "status", "DOWN",
                "error", e.getMessage()
            ));
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para obtener configuración del frontend
     */
    @GetMapping("/config/frontend")
    public ResponseEntity<Map<String, Object>> getFrontendConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("apiUrl", "http://localhost:8080/api");
        config.put("imageProxyUrl", "http://localhost:8080/api/proxy/image");
        config.put("supportedImageFormats", new String[]{"jpeg", "jpg", "png", "webp", "gif"});
        config.put("maxImageSize", "10MB");
        config.put("features", Map.of(
            "authentication", true,
            "cart", true,
            "wishlist", true,
            "orders", true,
            "categories", true,
            "search", true
        ));
        config.put("pagination", Map.of(
            "defaultPageSize", 12,
            "maxPageSize", 100
        ));
        
        return ResponseEntity.ok(config);
    }
}