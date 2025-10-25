package com.api.e_commerce.service;

import com.api.e_commerce.dto.PedidoDTO;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.PedidoRepository;
import com.api.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // IMPORTANTE
import org.springframework.security.core.Authentication; // IMPORTANTE
import org.springframework.security.core.context.SecurityContextHolder; // IMPORTANTE
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @SuppressWarnings("unused")
    @Autowired
    private ValidationService validationService; // Inyectar ValidationService
    
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        // Acceso ya restringido a ADMIN por el Controller
        return pedidoRepository.findAllWithUsuario().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PedidoDTO> obtenerPedidoPorId(Long id) {
        // La validación de ID no es necesaria si se hace en el Controller, pero la dejamos para robustez
        // validationService.validarId(id, "pedido"); 

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            return Optional.empty(); // Retorna 404
        }
        
        Pedido pedido = pedidoOpt.get();
        
        // --- ¡VERIFICAR PROPIEDAD! ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();

        // Permitir solo si es ADMIN o si el usuarioId del pedido coincide con el usuario logueado
        if (!usuarioActual.getRole().equals("ADMIN") && !pedido.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver este pedido.");
        }
        // --- FIN VERIFICACIÓN DE PROPIEDAD ---

        return Optional.of(convertirADTO(pedido));
    }
    
    public List<PedidoDTO> obtenerPedidosPorUsuario(Long usuarioId) {
        // La validación de propiedad ya está en el @PreAuthorize del Controller
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> pedidoRepository.findByUsuario(usuario).stream()
                        .map(this::convertirADTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
    
    public List<PedidoDTO> obtenerPedidosPorEstado(String estado) {
        // Acceso ya restringido a ADMIN por el Controller
        return pedidoRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public PedidoDTO crearPedido(PedidoDTO pedidoDTO) {
        // --- ¡FORZAR PROPIEDAD! ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        pedidoDTO.setUsuarioId(usuarioActual.getId()); // Forzamos el ID del usuario logueado
        // --- FIN FORZAR PROPIEDAD ---

        Optional<Usuario> usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId());
        
        if (usuario.isPresent()) {
            // Aquí irían validaciones adicionales, ej: validar el estado
            // validationService.validarValorPermitido(pedidoDTO.getEstado(), "estado", List.of("PENDIENTE")); 
            
            Pedido pedido = convertirAEntidad(pedidoDTO);
            pedido.setUsuario(usuario.get());
            pedido.setFecha(LocalDateTime.now());
            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            return convertirADTO(pedidoGuardado);
        }
        // Esto solo debería ocurrir si el usuario logueado no existe en BD, lo cual es un error grave.
        throw new RuntimeException("Usuario autenticado no encontrado en la base de datos."); 
    }
    
    public Optional<PedidoDTO> actualizarPedido(Long id, PedidoDTO pedidoDTO) {
        // Acceso ya restringido a ADMIN por el Controller
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    // Aquí iría la validación del estado por el ADMIN
                    pedido.setEstado(pedidoDTO.getEstado());
                    if (pedidoDTO.getFecha() != null) {
                        pedido.setFecha(pedidoDTO.getFecha());
                    }
                    return convertirADTO(pedidoRepository.save(pedido));
                });
    }
    
    public boolean eliminarPedido(Long id) {
        // Acceso ya restringido a ADMIN por el Controller
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private PedidoDTO convertirADTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setFecha(pedido.getFecha());
        dto.setEstado(pedido.getEstado());
        if (pedido.getUsuario() != null) {
            dto.setUsuarioId(pedido.getUsuario().getId());
            String nombreCompleto = "";
            if (pedido.getUsuario().getNombre() != null) {
                nombreCompleto += pedido.getUsuario().getNombre();
            }
            if (pedido.getUsuario().getApellido() != null) {
                if (!nombreCompleto.isEmpty()) {
                    nombreCompleto += " ";
                }
                nombreCompleto += pedido.getUsuario().getApellido();
            }
            dto.setUsuarioNombre(nombreCompleto);
        }
        return dto;
    }
    
    private Pedido convertirAEntidad(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setEstado(dto.getEstado());
        if (dto.getFecha() != null) {
            pedido.setFecha(dto.getFecha());
        }
        return pedido;
    }
}