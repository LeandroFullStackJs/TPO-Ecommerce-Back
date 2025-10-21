package com.api.e_commerce.service;

import com.api.e_commerce.dto.AuthResponse;
import com.api.e_commerce.dto.LoginRequest;
import com.api.e_commerce.dto.RegisterRequest;
import com.api.e_commerce.exception.DuplicateDataException;
import com.api.e_commerce.exception.UsuarioNotFoundException;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtService jwtService;

    @Autowired
    private ValidationService validationService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    }
    
    public AuthResponse register(RegisterRequest request) {
        // Validaciones
        validationService.validarEmail(request.getEmail());
        validationService.validarTextoNoVacio(request.getNombre(), "nombre");
        validationService.validarTextoNoVacio(request.getApellido(), "apellido");
        validationService.validarPassword(request.getPassword());
        
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateDataException("usuario", "email", request.getEmail());
        }
        
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRole("USER");
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        String jwt = jwtService.generateToken(usuarioGuardado);
        
        return new AuthResponse(
            jwt,
            usuarioGuardado.getId(),
            usuarioGuardado.getEmail(),
            usuarioGuardado.getNombre(),
            usuarioGuardado.getApellido(),
            usuarioGuardado.getRole()
        );
    }
    
    public AuthResponse authenticate(LoginRequest request) {
        // Validaciones básicas
        validationService.validarEmail(request.getEmail());
        validationService.validarTextoNoVacio(request.getPassword(), "password");
        
        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsuarioNotFoundException(request.getEmail()));
        
        String jwt = jwtService.generateToken(usuario);
        
        return new AuthResponse(
            jwt,
            usuario.getId(),
            usuario.getEmail(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getRole()
        );
    }
}