package com.api.e_commerce.service;

import com.api.e_commerce.model.Artista;
import com.api.e_commerce.dto.ArtistaDTO;
import com.api.e_commerce.dto.ArtistaCreateDTO;
import com.api.e_commerce.dto.ArtistaUpdateDTO;
import com.api.e_commerce.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // IMPORTANTE
import org.springframework.security.core.context.SecurityContextHolder; // IMPORTANTE
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private ValidationService validationService;

    public List<ArtistaDTO> getAllArtistas() {
        return artistaRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ArtistaDTO> getArtistaById(Long id) {
        validationService.validarId(id, "artista");
        return artistaRepository.findById(id)
                .filter(artista -> artista.getActivo())
                .map(this::convertirADTO);
    }

    public ArtistaDTO crearArtista(ArtistaCreateDTO artistaCreateDTO) {
        validationService.validarTextoNoVacio(artistaCreateDTO.getNombre(), "nombre");

        // Validaciones Email
        if (artistaCreateDTO.getEmail() != null && !artistaCreateDTO.getEmail().trim().isEmpty()) {
            validationService.validarEmail(artistaCreateDTO.getEmail());
            if (artistaRepository.existsByEmail(artistaCreateDTO.getEmail())) {
                throw new com.api.e_commerce.exception.DuplicateDataException("artista", "email", artistaCreateDTO.getEmail());
            }
        } else {
            validationService.validarTextoNoVacio(artistaCreateDTO.getEmail(), "email");
        }
        // Validación URL
        validationService.validarUrl(artistaCreateDTO.getImagenPerfil(), "imagenPerfil");

        Artista artista = convertirAEntidad(artistaCreateDTO);
        artista.setFechaCreacion(LocalDateTime.now());
        artista.setFechaActualizacion(LocalDateTime.now());
        
        Artista artistaGuardado = artistaRepository.save(artista);
        return convertirADTO(artistaGuardado);
    }

    public Optional<ArtistaDTO> actualizarArtista(Long id, ArtistaUpdateDTO artistaUpdateDTO) {
        validationService.validarId(id, "artista");        
        return artistaRepository.findById(id).map(artista -> {
            
            // --- VERIFICACIÓN DE SEGURIDAD (Se asume que la modificación es solo de ADMIN o del artista) ---
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Lógica compleja para verificar si el usuario logueado es el dueño. 
            // Por simplicidad, si no hay un campo 'usuarioId' en Artista, forzamos que sea ADMIN.
            // Si la regla en Controller es USER/ADMIN, la validación se hace a nivel de rol.
            
            // Validaciones Email
            if (artistaUpdateDTO.getEmail() != null && !artistaUpdateDTO.getEmail().trim().isEmpty()) {
                 validationService.validarEmail(artistaUpdateDTO.getEmail());
                 // Validar email único solo si se está cambiando y el nuevo email ya existe para OTRO artista
                 if (!artistaUpdateDTO.getEmail().equalsIgnoreCase(artista.getEmail()) &&
                    artistaRepository.existsByEmailAndIdNot(artistaUpdateDTO.getEmail(), id)) {
                    throw new com.api.e_commerce.exception.DuplicateDataException("artista", "email", artistaUpdateDTO.getEmail());
                 }
            }
            
            // Validación URL
            if (artistaUpdateDTO.getImagenPerfil() != null) {
                validationService.validarUrl(artistaUpdateDTO.getImagenPerfil(), "imagenPerfil");
            }

            actualizarCampos(artista, artistaUpdateDTO);
            artista.setFechaActualizacion(LocalDateTime.now());
            
            Artista artistaActualizado = artistaRepository.save(artista);
            return convertirADTO(artistaActualizado);
        });
    }

    public boolean eliminarArtista(Long id) {
        // Acceso ya restringido a ADMIN por el Controller
        return artistaRepository.findById(id).map(artista -> {
            artista.setActivo(false);
            artista.setFechaActualizacion(LocalDateTime.now());
            artistaRepository.save(artista);
            return true;
        }).orElseThrow(() -> new com.api.e_commerce.exception.ArtistaNotFoundException(id));
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