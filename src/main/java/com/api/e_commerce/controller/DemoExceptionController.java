package com.api.e_commerce.controller;

import com.api.e_commerce.dto.ProductoDTO;
import com.api.e_commerce.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de ejemplo para mostrar el manejo de excepciones
 * Este endpoint es solo para demostración
 */
@RestController
@RequestMapping("/api/demo")
@CrossOrigin(origins = "*")
public class DemoExceptionController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private com.api.e_commerce.service.ValidationService validationService;

    /**
     * Endpoint para probar el manejo de ProductoNotFoundException
     * GET /api/demo/producto-no-encontrado/999
     */
    @GetMapping("/producto-no-encontrado/{id}")
    public ResponseEntity<ProductoDTO> demoProductoNoEncontrado(@PathVariable Long id) {
        // Esto lanzará ProductoNotFoundException si el ID no existe
        // El GlobalExceptionHandler lo convertirá en una respuesta JSON estructurada
        return productoService.getProductoById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.api.e_commerce.exception.ProductoNotFoundException(id));
    }

    /**
     * Endpoint para probar validación de ID inválido
     * GET /api/demo/validacion-id/-1
     */
    @GetMapping("/validacion-id/{id}")
    public ResponseEntity<String> demoValidacionId(@PathVariable Long id) {
        // Esto lanzará InvalidDataException si el ID es inválido
        validationService.validarId(id, "producto");
        
        return ResponseEntity.ok("ID válido: " + id);
    }

    /**
     * Endpoint para probar validación de precio
     * POST /api/demo/validar-precio?precio=-10
     */
    @PostMapping("/validar-precio")
    public ResponseEntity<String> demoValidacionPrecio(@RequestParam Double precio) {
        // Esto lanzará PrecioNegativoException si el precio es negativo
        validationService.validarPrecio(precio);
        
        return ResponseEntity.ok("Precio válido: " + precio);
    }

    /**
     * Endpoint para probar datos duplicados
     * POST /api/demo/datos-duplicados?email=test@test.com
     */
    @PostMapping("/datos-duplicados")
    public ResponseEntity<String> demoDatosDuplicados(@RequestParam String email) {
        // Simular verificación de email duplicado
        if ("test@test.com".equals(email)) {
            throw new com.api.e_commerce.exception.DuplicateDataException("usuario", "email", email);
        }
        
        return ResponseEntity.ok("Email disponible: " + email);
    }

    /**
     * Endpoint para probar error de argumentos inválidos
     * GET /api/demo/argumento-invalido?texto=
     */
    @GetMapping("/argumento-invalido")
    public ResponseEntity<String> demoArgumentoInvalido(@RequestParam String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El parámetro 'texto' no puede estar vacío");
        }
        
        return ResponseEntity.ok("Texto válido: " + texto);
    }

    /**
     * Endpoint para probar error interno
     * GET /api/demo/error-interno
     */
    @GetMapping("/error-interno")
    public ResponseEntity<String> demoErrorInterno() {
        // Simular un error interno
        throw new RuntimeException("Esto es un error interno simulado");
    }

    /**
     * Información sobre cómo probar el sistema de excepciones
     */
    @GetMapping("/info")
    public ResponseEntity<String> info() {
        StringBuilder info = new StringBuilder();
        info.append("Endpoints para probar el manejo de excepciones:\n\n");
        info.append("1. Producto no encontrado: GET /api/demo/producto-no-encontrado/999\n");
        info.append("2. ID inválido: GET /api/demo/validacion-id/-1\n");
        info.append("3. Precio negativo: POST /api/demo/validar-precio?precio=-10\n");
        info.append("4. Datos duplicados: POST /api/demo/datos-duplicados?email=test@test.com\n");
        info.append("5. Argumento inválido: GET /api/demo/argumento-invalido?texto=\n");
        info.append("6. Error interno: GET /api/demo/error-interno\n\n");
        info.append("Todas las respuestas de error seguirán el formato estándar de ErrorResponse.\n");
        info.append("Ver EXCEPTION_HANDLING.md para más detalles.");
        
        return ResponseEntity.ok(info.toString());
    }
}