package com.api.e_commerce.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.api.e_commerce.dto.UsuarioDTO;
import com.api.e_commerce.service.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = {"http://localhost:5173"})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para debug - listar todos los emails sin autenticación
    @GetMapping("/debug/emails")
    public ResponseEntity<List<String>> getEmails() {
        List<String> emails = usuarioService.getAllEmails();
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.esPropietario(authentication, #id)")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        // El servicio ahora devuelve un DTO directamente o lanza una excepción.
        // El código del controlador es simple y directo.
        UsuarioDTO usuarioDTO = usuarioService.obtenerUsuarioPorId(id);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityService.esPropietario(authentication, #id)")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        // El servicio maneja todas las excepciones (NotFound, InvalidData).
        // El controlador solo llama y devuelve la respuesta exitosa.
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        // Ya no se comprueba un booleano. El servicio lanza una excepción si el usuario no existe.
        // Si el método termina sin excepción, la operación fue exitosa.
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // 204 No Content es la respuesta estándar para DELETE.
    }

    @PatchMapping("/{id}/password") // Usar PATCH para actualizaciones parciales como la contraseña
    @PreAuthorize("@securityService.esPropietario(authentication, #id)")
    public ResponseEntity<Void> cambiarPassword(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        // Se elimina el try-catch. Las excepciones (NotFound, InvalidPassword)
        // son manejadas por el GlobalExceptionHandler.
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");
        usuarioService.cambiarPassword(id, currentPassword, newPassword);
        return ResponseEntity.ok().build(); // 200 OK para confirmar el éxito.
    }

}
