package com.api.e_commerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import com.api.e_commerce.dto.UsuarioDTO;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.UsuarioRepository;
import com.api.e_commerce.exception.UsuarioNotFoundException; // IMPORTANTE

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ValidationService validationService; // IMPORTANTE: Inyectar ValidationService

    //getAllUsuarios 
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long id) {
        validationService.validarId(id, "usuario"); // Validar ID
        return usuarioRepository.findById(id)
                .map(this::convertirADTO);
    }

    //saveUsuario
    public UsuarioDTO save(Usuario usuario) {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }
    
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Aquí irían validaciones de email duplicado si no se usa el endpoint /auth/register
        Usuario usuario = convertirAEntidad(usuarioDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }
    
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        validationService.validarId(id, "usuario"); // Validar ID
        
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    // Aquí se puede agregar lógica de seguridad para verificar propiedad si es necesario
                    usuario.setNombre(usuarioDTO.getNombre());
                    usuario.setApellido(usuarioDTO.getApellido());
                    // Si se actualiza el email, se necesitaría más validación de unicidad
                    usuario.setEmail(usuarioDTO.getEmail());
                    return convertirADTO(usuarioRepository.save(usuario));
                })
                .orElseThrow(() -> new UsuarioNotFoundException(id)); // Lanzar excepción si no se encuentra
    }
    
    public boolean eliminarUsuario(Long id) {
        validationService.validarId(id, "usuario"); // Validar ID
        
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        // No incluimos password por seguridad
        return dto;
    }
    
    private Usuario convertirAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        // Password se manejará por separado
        return usuario;
    }
}
