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
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return List.of();
        }
        Usuario usuario = usuarioOpt.get();
        return direccionRepository.findByUsuario_Id(usuario.getId()).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<DireccionDTO> obtenerTodasLasDirecciones() {
        return direccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DireccionDTO> obtenerDireccionPorId(Long id) {
        return direccionRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    public DireccionDTO crearDireccion(DireccionDTO direccionDTO) {
        Direccion direccion = convertirAEntidad(direccionDTO);
        
        // Asociar automáticamente con el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            if (usuarioOpt.isPresent()) {
                direccion.setUsuario(usuarioOpt.get());
            }
        }
        
        Direccion direccionGuardada = direccionRepository.save(direccion);
        return convertirADTO(direccionGuardada);
    }
    
    public Optional<DireccionDTO> actualizarDireccion(Long id, DireccionDTO direccionDTO) {
        return direccionRepository.findById(id)
                .map(direccion -> {
                    direccion.setCalle(direccionDTO.getCalle());
                    direccion.setNumero(direccionDTO.getNumero());
                    direccion.setLocalidad(direccionDTO.getLocalidad());
                    direccion.setProvincia(direccionDTO.getProvincia());
                    direccion.setPais(direccionDTO.getPais());
                    return convertirADTO(direccionRepository.save(direccion));
                });
    }
    
    public boolean eliminarDireccion(Long id) {
        if (direccionRepository.existsById(id)) {
            direccionRepository.deleteById(id);
            return true;
        }
        return false;
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