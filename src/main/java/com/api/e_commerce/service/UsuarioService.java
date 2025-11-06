package com.api.e_commerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import com.api.e_commerce.dto.UsuarioDTO;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.UsuarioRepository;
import com.api.e_commerce.exception.InvalidPasswordException;
import com.api.e_commerce.exception.InvalidUserDataException;
import com.api.e_commerce.exception.UsuarioNotFoundException; // IMPORTANTE
import com.api.e_commerce.exception.DuplicateDataException;
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

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        validationService.validarId(id, "usuario"); // Validar ID
        return usuarioRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    //saveUsuario
    public UsuarioDTO save(Usuario usuario) {
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }
    
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        // Validar que los datos básicos (nombre, apellido, email) no estén vacíos.
        validarDatosUsuario(usuarioDTO);

        // Validar que el email no esté ya registrado.
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail().trim())) {
            throw new DuplicateDataException("usuario", "email", usuarioDTO.getEmail().trim());
        }

        // Si las validaciones pasan, se procede a crear el usuario.
        Usuario usuario = convertirAEntidad(usuarioDTO);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }
    
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        validationService.validarId(id, "usuario"); // Validar ID
        validarDatosUsuario(usuarioDTO);
        
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNombre(usuarioDTO.getNombre().trim());
                    usuario.setApellido(usuarioDTO.getApellido().trim());

                    // Validación de email duplicado al actualizar
                    String nuevoEmail = usuarioDTO.getEmail().trim();
                    if (!nuevoEmail.equalsIgnoreCase(usuario.getEmail()) && usuarioRepository.existsByEmail(nuevoEmail)) {
                        throw new DuplicateDataException("usuario", "email", nuevoEmail);
                    }

                    usuario.setEmail(usuarioDTO.getEmail().trim());
                    return convertirADTO(usuarioRepository.save(usuario));
                })
                .orElseThrow(() -> new UsuarioNotFoundException(id)); // Lanzar excepción si no se encuentra
    }
    
    public boolean eliminarUsuario(Long id) {
        validationService.validarId(id, "usuario");

        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
        return true; // Se puede mantener o cambiar el método a void
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
            throw new InvalidPasswordException("La contraseña actual es incorrecta");
        }
        
        // Validar nueva contraseña
        if (newPassword == null || newPassword.trim().length() < 6) {
            throw new InvalidPasswordException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        // Encriptar la nueva contraseña antes de guardarla
        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);
    }

    private void validarDatosUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
            throw new InvalidUserDataException("El nombre es obligatorio");
        }
        if (usuarioDTO.getApellido() == null || usuarioDTO.getApellido().trim().isEmpty()) {
            throw new InvalidUserDataException("El apellido es obligatorio");
        }
        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new InvalidUserDataException("El email es obligatorio");
        }
    }
}
