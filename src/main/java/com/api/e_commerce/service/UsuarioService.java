package com.api.e_commerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import com.api.e_commerce.dto.UsuarioDTO;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.UsuarioRepository;
import com.api.e_commerce.exception.DuplicateDataException;
import com.api.e_commerce.exception.UsuarioNotFoundException; // IMPORTANTE

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ValidationService validationService; // IMPORTANTE: Inyectar ValidationService

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectar PasswordEncoder

    //getAllUsuarios 
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método para debug - obtener todos los emails
    public List<String> getAllEmails() {
        return usuarioRepository.findAll().stream()
                .map(Usuario::getEmail)
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
        
        // Usar ValidationService para centralizar las validaciones
        validationService.validarTextoNoVacio(usuarioDTO.getNombre(), "nombre");
        validationService.validarTextoNoVacio(usuarioDTO.getApellido(), "apellido");
        validationService.validarEmail(usuarioDTO.getEmail());
        
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    // Verificar si el email ha cambiado y si el nuevo ya existe
                    String nuevoEmail = usuarioDTO.getEmail().trim();
                    if (!usuario.getEmail().equalsIgnoreCase(nuevoEmail)) {
                        if (usuarioRepository.existsByEmail(nuevoEmail)) {
                            throw new DuplicateDataException("usuario", "email", nuevoEmail);
                        }
                        usuario.setEmail(nuevoEmail);
                    }

                    usuario.setNombre(usuarioDTO.getNombre().trim());
                    usuario.setApellido(usuarioDTO.getApellido().trim());
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

    public void cambiarPassword(Long id, String currentPassword, String newPassword) {
        validationService.validarId(id, "usuario");
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
        
        // Validar contraseña actual usando el encoder
        if (currentPassword != null && !passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        
        // Validar nueva contraseña
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new IllegalArgumentException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        // Encriptar la nueva contraseña antes de guardarla
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);
    }
}
