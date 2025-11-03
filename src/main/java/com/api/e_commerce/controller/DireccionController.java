package com.api.e_commerce.controller;

import com.api.e_commerce.dto.DireccionDTO;
import com.api.e_commerce.service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; 
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/direcciones")
@CrossOrigin(origins = {"http://localhost:5173"})
public class DireccionController {

    @Autowired
    private DireccionService direccionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<DireccionDTO>> obtenerTodasLasDirecciones() {
        List<DireccionDTO> direcciones = direccionService.obtenerTodasLasDirecciones();
        return ResponseEntity.ok(direcciones);
    }

    // IMPORTANTE: Este endpoint debe estar ANTES de /{id} para que Spring lo capture correctamente
    @GetMapping("/mias")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<DireccionDTO>> obtenerMisDirecciones() {
        List<DireccionDTO> direcciones = direccionService.obtenerMisDirecciones();
        return ResponseEntity.ok(direcciones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<DireccionDTO> obtenerDireccionPorId(@PathVariable Long id) {
        Optional<DireccionDTO> direccion = direccionService.obtenerDireccionPorId(id);
        return direccion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Mantenemos 404 si el servicio devuelve vacío
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DireccionDTO> crearDireccion(@Valid @RequestBody DireccionDTO direccionDTO) {
        // Se quita el try-catch.
        DireccionDTO nuevaDireccion = direccionService.crearDireccion(direccionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDireccion);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DireccionDTO> actualizarDireccion(@PathVariable Long id, @Valid @RequestBody DireccionDTO direccionDTO) {
        Optional<DireccionDTO> direccionActualizada = direccionService.actualizarDireccion(id, direccionDTO);
        return direccionActualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); // Mantenemos 404 si el servicio devuelve vacío
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long id) {
        if (direccionService.eliminarDireccion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build(); // Mantenemos 404 si el servicio devuelve false
    }
}