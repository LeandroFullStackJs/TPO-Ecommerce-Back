package com.api.e_commerce.controller;

import com.api.e_commerce.dto.PedidoDTO;
import com.api.e_commerce.exception.PedidoNotFoundException; // Importar excepción
import com.api.e_commerce.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid; 

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = {"http://localhost:5173"})
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoDTO>> obtenerTodosLosPedidos() {
        List<PedidoDTO> pedidos = pedidoService.obtenerTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Servicio verifica propiedad
    public ResponseEntity<PedidoDTO> obtenerPedidoPorId(@PathVariable Long id) {
        // Dejamos que el service lance AccessDeniedException o PedidoNotFoundException
        Optional<PedidoDTO> pedido = pedidoService.obtenerPedidoPorId(id);
        return pedido.map(ResponseEntity::ok)
                 .orElseThrow(() -> new PedidoNotFoundException(id)); // Lanzar excepción para 404
    }

    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN') or #usuarioId == authentication.principal.id")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorUsuario(@PathVariable Long usuarioId) {
        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPorUsuario(usuarioId);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoDTO>> obtenerPedidosPorEstado(@PathVariable String estado) {
        List<PedidoDTO> pedidos = pedidoService.obtenerPedidosPorEstado(estado);
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        // Se quita el try-catch. El servicio forzará el usuarioId correcto.
        PedidoDTO nuevoPedido = pedidoService.crearPedido(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO pedidoDTO) {
        // Dejamos que el service lance PedidoNotFoundException si no existe
        Optional<PedidoDTO> pedidoActualizado = pedidoService.actualizarPedido(id, pedidoDTO);
        return pedidoActualizado.map(ResponseEntity::ok)
                 .orElseThrow(() -> new PedidoNotFoundException(id)); // Lanzar excepción para 404
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build();
        } else {
             // Si eliminarPedido devuelve false porque no existe, lanzar excepción
             throw new PedidoNotFoundException(id);
        }
    }
}