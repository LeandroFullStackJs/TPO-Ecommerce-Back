package com.api.e_commerce.service;

import com.api.e_commerce.dto.PedidoDTO;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.Usuario;
import com.api.e_commerce.repository.PedidoRepository;
import com.api.e_commerce.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    public List<PedidoDTO> obtenerTodosLosPedidos() {
        return pedidoRepository.findAllWithUsuario().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public Optional<PedidoDTO> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    public List<PedidoDTO> obtenerPedidosPorUsuario(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> pedidoRepository.findByUsuario(usuario).stream()
                        .map(this::convertirADTO)
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }
    
    public List<PedidoDTO> obtenerPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public PedidoDTO crearPedido(PedidoDTO pedidoDTO) {
        Optional<Usuario> usuario = usuarioRepository.findById(pedidoDTO.getUsuarioId());
        if (usuario.isPresent()) {
            Pedido pedido = convertirAEntidad(pedidoDTO);
            pedido.setUsuario(usuario.get());
            pedido.setFecha(LocalDateTime.now());
            Pedido pedidoGuardado = pedidoRepository.save(pedido);
            return convertirADTO(pedidoGuardado);
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + pedidoDTO.getUsuarioId());
    }
    
    public Optional<PedidoDTO> actualizarPedido(Long id, PedidoDTO pedidoDTO) {
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setEstado(pedidoDTO.getEstado());
                    if (pedidoDTO.getFecha() != null) {
                        pedido.setFecha(pedidoDTO.getFecha());
                    }
                    return convertirADTO(pedidoRepository.save(pedido));
                });
    }
    
    public boolean eliminarPedido(Long id) {
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