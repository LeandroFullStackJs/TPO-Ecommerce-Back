package com.api.e_commerce.service;

import com.api.e_commerce.model.Artista;
import com.api.e_commerce.dto.ArtistaDTO;
import com.api.e_commerce.dto.ArtistaCreateDTO;
import com.api.e_commerce.dto.ArtistaUpdateDTO;
import com.api.e_commerce.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    public List<ArtistaDTO> getAllArtistas() {
        return artistaRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ArtistaDTO> getArtistaById(Long id) {
        return artistaRepository.findById(id)
                .filter(artista -> artista.getActivo())
                .map(this::convertirADTO);
    }

    public ArtistaDTO crearArtista(ArtistaCreateDTO artistaCreateDTO) {
        // Validar email único
        if (artistaCreateDTO.getEmail() != null && artistaRepository.existsByEmail(artistaCreateDTO.getEmail())) {
            throw new RuntimeException("Ya existe un artista con este email");
        }
        
        Artista artista = convertirAEntidad(artistaCreateDTO);
        artista.setFechaCreacion(LocalDateTime.now());
        artista.setFechaActualizacion(LocalDateTime.now());
        
        Artista artistaGuardado = artistaRepository.save(artista);
        return convertirADTO(artistaGuardado);
    }

    public Optional<ArtistaDTO> actualizarArtista(Long id, ArtistaUpdateDTO artistaUpdateDTO) {
        return artistaRepository.findById(id).map(artista -> {
            // Validar email único si se está cambiando
            if (artistaUpdateDTO.getEmail() != null && 
                !artistaUpdateDTO.getEmail().equals(artista.getEmail()) &&
                artistaRepository.existsByEmailAndIdNot(artistaUpdateDTO.getEmail(), id)) {
                throw new RuntimeException("Ya existe un artista con este email");
            }
            
            actualizarCampos(artista, artistaUpdateDTO);
            artista.setFechaActualizacion(LocalDateTime.now());
            
            Artista artistaActualizado = artistaRepository.save(artista);
            return convertirADTO(artistaActualizado);
        });
    }

    public boolean eliminarArtista(Long id) {
        return artistaRepository.findById(id).map(artista -> {
            artista.setActivo(false);
            artista.setFechaActualizacion(LocalDateTime.now());
            artistaRepository.save(artista);
            return true;
        }).orElse(false);
    }

    public List<ArtistaDTO> buscarArtistas(String nombre) {
        return artistaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private void actualizarCampos(Artista artista, ArtistaUpdateDTO dto) {
        if (dto.getNombre() != null) artista.setNombre(dto.getNombre());
        if (dto.getBiografia() != null) artista.setBiografia(dto.getBiografia());
        if (dto.getImagenPerfil() != null) artista.setImagenPerfil(dto.getImagenPerfil());
        if (dto.getEmail() != null) artista.setEmail(dto.getEmail());
        if (dto.getActivo() != null) artista.setActivo(dto.getActivo());
    }
    
    private ArtistaDTO convertirADTO(Artista artista) {
        ArtistaDTO dto = new ArtistaDTO();
        dto.setId(artista.getId());
        dto.setNombre(artista.getNombre());
        dto.setBiografia(artista.getBiografia());
        dto.setImagenPerfil(artista.getImagenPerfil());
        dto.setEmail(artista.getEmail());
        dto.setActivo(artista.getActivo());
        dto.setFechaCreacion(artista.getFechaCreacion());
        dto.setFechaActualizacion(artista.getFechaActualizacion());
        return dto;
    }
    
    private Artista convertirAEntidad(ArtistaCreateDTO dto) {
        Artista artista = new Artista();
        artista.setNombre(dto.getNombre());
        artista.setBiografia(dto.getBiografia());
        artista.setImagenPerfil(dto.getImagenPerfil());
        artista.setEmail(dto.getEmail());
        artista.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        return artista;
    }
}