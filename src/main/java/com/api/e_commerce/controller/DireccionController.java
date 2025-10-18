package com.api.e_commerce.controller;

import com.api.e_commerce.dto.DireccionDTO;
import com.api.e_commerce.service.DireccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/direcciones")
@CrossOrigin(origins = "*")
public class DireccionController {
    
    @Autowired
    private DireccionService direccionService;
    
    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerTodasLasDirecciones() {
        List<DireccionDTO> direcciones = direccionService.obtenerTodasLasDirecciones();
        return ResponseEntity.ok(direcciones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerDireccionPorId(@PathVariable Long id) {
        Optional<DireccionDTO> direccion = direccionService.obtenerDireccionPorId(id);
        return direccion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DireccionDTO> crearDireccion(@RequestBody DireccionDTO direccionDTO) {
        try {
            DireccionDTO nuevaDireccion = direccionService.crearDireccion(direccionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaDireccion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizarDireccion(@PathVariable Long id, @RequestBody DireccionDTO direccionDTO) {
        Optional<DireccionDTO> direccionActualizada = direccionService.actualizarDireccion(id, direccionDTO);
        return direccionActualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDireccion(@PathVariable Long id) {
        if (direccionService.eliminarDireccion(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}