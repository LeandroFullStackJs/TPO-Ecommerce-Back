package com.api.e_commerce.controller;

import com.api.e_commerce.dto.ArtistaDTO;
import com.api.e_commerce.dto.ArtistaCreateDTO;
import com.api.e_commerce.dto.ArtistaUpdateDTO;
import com.api.e_commerce.service.ArtistaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    @GetMapping
    public ResponseEntity<List<ArtistaDTO>> getAllArtistas() {
        List<ArtistaDTO> artistas = artistaService.getAllArtistas();
        return ResponseEntity.ok(artistas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaDTO> getArtistaById(@PathVariable Long id) {
        return artistaService.getArtistaById(id)
                .map(artista -> ResponseEntity.ok(artista))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ArtistaDTO>> buscarArtistas(@RequestParam String q) {
        List<ArtistaDTO> artistas = artistaService.buscarArtistas(q);
        return ResponseEntity.ok(artistas);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ArtistaDTO> crearArtista(@Valid @RequestBody ArtistaCreateDTO artistaCreateDTO) {
        try {
            ArtistaDTO nuevoArtista = artistaService.crearArtista(artistaCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoArtista);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ArtistaDTO> actualizarArtista(
            @PathVariable Long id, 
            @Valid @RequestBody ArtistaUpdateDTO artistaUpdateDTO) {
        try {
            return artistaService.actualizarArtista(id, artistaUpdateDTO)
                    .map(artista -> ResponseEntity.ok(artista))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarArtista(@PathVariable Long id) {
        if (artistaService.eliminarArtista(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}