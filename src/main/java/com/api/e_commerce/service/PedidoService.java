package com.api.e_commerce.service;

import com.api.e_commerce.dto.PedidoDTO;
import com.api.e_commerce.dto.PedidoItemDTO;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.PedidoItem;
import com.api.e_commerce.model.Producto;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.PedidoRepository;
import com.api.e_commerce.repository.ProductoRepository;
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
    
    @Autowired
    private ProductoRepository productoRepository;

    @SuppressWarnings("unused")
    @Autowired
    private ValidationService validationService; // Inyectar ValidationService
    
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        // Acceso ya restringido a ADMIN por el Controller
        return pedidoRepository.findAllWithUsuario().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public PedidoDTO obtenerPedidoPorId(Long id) {
        // La validación de ID no es necesaria si se hace en el Controller, pero la dejamos para robustez
        // validationService.validarId(id, "pedido"); 

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new com.api.e_commerce.exception.PedidoNotFoundException("No se encontró el pedido con id: " + id));
        
        // --- ¡VERIFICAR PROPIEDAD! ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();

        // Permitir solo si es ADMIN o si el usuarioId del pedido coincide con el usuario logueado
        if (!usuarioActual.getRole().equals("ADMIN") && !pedido.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new AccessDeniedException("No tienes permiso para ver este pedido.");
        }
        // --- FIN VERIFICACIÓN DE PROPIEDAD ---

        return convertirADTO(pedido);
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
                    if (pedidoDTO.getEstado() != null) {
                        pedido.setEstado(Pedido.EstadoPedido.valueOf(pedidoDTO.getEstado()));
                    }
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
        
        // Manejo seguro del estado
        if (pedido.getEstado() != null) {
            dto.setEstado(pedido.getEstado().name());
        } else {
            dto.setEstado("PENDIENTE"); // Valor por defecto
        }
        
        dto.setTotal(pedido.getTotal() != null ? pedido.getTotal() : 0.0);
        dto.setNotas(pedido.getNotas());
        dto.setFechaActualizacion(pedido.getFechaActualizacion());
        
        // Manejo seguro del usuario
        if (pedido.getUsuario() != null) {
            try {
                dto.setUsuarioId(pedido.getUsuario().getId());
                dto.setUsuarioEmail(pedido.getUsuario().getEmail());
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
            } catch (Exception e) {
                // Si falla el acceso lazy, usar valores por defecto
                dto.setUsuarioId(null);
                dto.setUsuarioEmail("usuario_no_disponible");
                dto.setUsuarioNombre("Usuario No Disponible");
            }
        }
        
        // Agregar información de dirección de envío si existe
        if (pedido.getDireccionEnvio() != null) {
            try {
                dto.setDireccionEnvioId(pedido.getDireccionEnvio().getId());
            } catch (Exception e) {
                // Ignorar si falla el acceso lazy
                dto.setDireccionEnvioId(null);
            }
        }
        
        return dto;
    }
    
    private Pedido convertirAEntidad(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        
        // Establecer campos básicos
        if (dto.getEstado() != null) {
            pedido.setEstado(Pedido.EstadoPedido.valueOf(dto.getEstado()));
        } else {
            pedido.setEstado(Pedido.EstadoPedido.PENDIENTE); // Estado por defecto
        }
        
        if (dto.getFecha() != null) {
            pedido.setFecha(dto.getFecha());
        } else {
            pedido.setFecha(LocalDateTime.now()); // Fecha actual por defecto
        }
        
        if (dto.getTotal() != null) {
            pedido.setTotal(dto.getTotal());
        }
        
        if (dto.getNotas() != null) {
            pedido.setNotas(dto.getNotas());
        }
        
        // Procesar items del pedido
        if (dto.getItems() != null && !dto.getItems().isEmpty()) {
            for (PedidoItemDTO itemDTO : dto.getItems()) {
                PedidoItem item = new PedidoItem();
                item.setCantidad(itemDTO.getCantidad());
                item.setPrecioUnitario(itemDTO.getPrecioUnitario());
                
                // Calcular subtotal si no viene en el DTO
                if (itemDTO.getSubtotal() != null) {
                    item.setSubtotal(itemDTO.getSubtotal());
                } else if (itemDTO.getCantidad() != null && itemDTO.getPrecioUnitario() != null) {
                    item.setSubtotal(itemDTO.getCantidad() * itemDTO.getPrecioUnitario());
                }
                
                // Buscar y asignar el producto
                if (itemDTO.getProductoId() != null) {
                    Optional<Producto> producto = productoRepository.findById(itemDTO.getProductoId());
                    if (producto.isPresent()) {
                        item.setProducto(producto.get());
                    }
                }
                
                // Establecer relación bidireccional
                item.setPedido(pedido);
                pedido.getItems().add(item);
            }
        }
        
        return pedido;
    }
}