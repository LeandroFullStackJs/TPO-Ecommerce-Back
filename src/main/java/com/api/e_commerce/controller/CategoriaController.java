package com.api.e_commerce.controller;

import com.api.e_commerce.dto.CategoriaDTO;
import com.api.e_commerce.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; 

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerTodasLasCategorias() {
        List<CategoriaDTO> categorias = categoriaService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorId(@PathVariable Long id) {
        // Dejamos que el service lance CategoriaNotFoundException si no existe
        Optional<CategoriaDTO> categoria = categoriaService.obtenerCategoriaPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElseThrow(() -> new com.api.e_commerce.exception.CategoriaNotFoundException(id)); // Lanzar excepción para 404
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CategoriaDTO> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        Optional<CategoriaDTO> categoria = categoriaService.obtenerCategoriaPorNombre(nombre);
        return categoria.map(ResponseEntity::ok)
                 .orElseThrow(() -> new com.api.e_commerce.exception.CategoriaNotFoundException(nombre)); // Lanzar excepción para 404
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> crearCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        // Se quita el try-catch.
        CategoriaDTO nuevaCategoria = categoriaService.crearCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaDTO categoriaDTO) {
        // Dejamos que el service lance CategoriaNotFoundException si no existe
        Optional<CategoriaDTO> categoriaActualizada = categoriaService.actualizarCategoria(id, categoriaDTO);
        return categoriaActualizada.map(ResponseEntity::ok)
                 .orElseThrow(() -> new com.api.e_commerce.exception.CategoriaNotFoundException(id)); // Lanzar excepción para 404
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        if (categoriaService.eliminarCategoria(id)) {
            return ResponseEntity.noContent().build();
        } else {
             // Si eliminarCategoria devuelve false porque no existe, lanzar excepción
             throw new com.api.e_commerce.exception.CategoriaNotFoundException(id);
        }
    }
}