package com.api.e_commerce.service;

import com.api.e_commerce.dto.DireccionDTO;
import com.api.e_commerce.model.Direccion;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.DireccionRepository;
import com.api.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.api.e_commerce.exception.DireccionNotFoundException;
import com.api.e_commerce.exception.UsuarioNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DireccionService {
    
    @Autowired
    private DireccionRepository direccionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional(readOnly = true)
    public List<DireccionDTO> obtenerMisDirecciones() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (authentication != null) ? authentication.getName() : null;
        if (email == null) {
            return List.of();
        }
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con email: " + email));
        
        return direccionRepository.findByUsuario_Id(usuario.getId()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<DireccionDTO> obtenerTodasLasDirecciones() {
        return direccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public DireccionDTO obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new DireccionNotFoundException(id));
    }
    
    public DireccionDTO crearDireccion(DireccionDTO direccionDTO) {
        Direccion direccion = convertirAEntidad(direccionDTO);
        
        // Asociar automáticamente con el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no autenticado o no encontrado."));
            direccion.setUsuario(usuario);
        } else {
             throw new UsuarioNotFoundException("No hay un usuario autenticado para asociar la dirección.");
        }
        
        Direccion direccionGuardada = direccionRepository.save(direccion);
        return convertirADTO(direccionGuardada);
    }
    
    public DireccionDTO actualizarDireccion(Long id, DireccionDTO direccionDTO) {
        Direccion direccion = direccionRepository.findById(id)
            .orElseThrow(() -> new DireccionNotFoundException(id));

        direccion.setCalle(direccionDTO.getCalle());
        direccion.setNumero(direccionDTO.getNumero());
        direccion.setLocalidad(direccionDTO.getLocalidad());
        direccion.setProvincia(direccionDTO.getProvincia());
        direccion.setPais(direccionDTO.getPais());
        
        Direccion direccionActualizada = direccionRepository.save(direccion);
        return convertirADTO(direccionActualizada);
    }
    
    public void eliminarDireccion(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new DireccionNotFoundException(id);
        }
        direccionRepository.deleteById(id);
    }
    
    private DireccionDTO convertirADTO(Direccion direccion) {
        DireccionDTO dto = new DireccionDTO();
        dto.setId(direccion.getId());
        dto.setCalle(direccion.getCalle());
        dto.setNumero(direccion.getNumero());
        dto.setLocalidad(direccion.getLocalidad());
        dto.setProvincia(direccion.getProvincia());
        dto.setPais(direccion.getPais());
        dto.setCodigoPostal(direccion.getCodigoPostal());
        dto.setObservaciones(direccion.getObservaciones());
        dto.setEsPrincipal(direccion.getEsPrincipal());
        // Acceso seguro al usuario para evitar LazyInitializationException
        try {
            if (direccion.getUsuario() != null) {
                dto.setUsuarioId(direccion.getUsuario().getId());
            }
        } catch (Exception e) {
            // Si hay error de lazy loading, dejar usuarioId como null
            dto.setUsuarioId(null);
        }
        return dto;
    }
    
    private Direccion convertirAEntidad(DireccionDTO dto) {
        Direccion direccion = new Direccion();
        direccion.setCalle(dto.getCalle());
        direccion.setNumero(dto.getNumero());
        direccion.setLocalidad(dto.getLocalidad());
        direccion.setProvincia(dto.getProvincia());
        direccion.setPais(dto.getPais());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setObservaciones(dto.getObservaciones());
        direccion.setEsPrincipal(dto.getEsPrincipal());
        return direccion;
    }
}