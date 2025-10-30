package com.api.e_commerce.controller;

import com.api.e_commerce.dto.PedidoItemDTO;
import com.api.e_commerce.service.PedidoItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para gestionar items de pedidos
 */
@RestController
@RequestMapping("/api/pedido-items")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4173"})
public class PedidoItemController {

    @Autowired
    private PedidoItemService pedidoItemService;

    /**
     * Obtener todos los items de un pedido específico
     */
    @GetMapping("/pedido/{pedidoId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PedidoItemDTO>> obtenerItemsPorPedido(@PathVariable Long pedidoId) {
        List<PedidoItemDTO> items = pedidoItemService.obtenerItemsPorPedido(pedidoId);
        return ResponseEntity.ok(items);
    }

    /**
     * Obtener un item específico por ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoItemDTO> obtenerItemPorId(@PathVariable Long id) {
        return pedidoItemService.obtenerItemPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo item en un pedido
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoItemDTO> crearItem(@Valid @RequestBody PedidoItemDTO itemDTO) {
        PedidoItemDTO nuevoItem = pedidoItemService.crearItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoItem);
    }

    /**
     * Actualizar un item existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PedidoItemDTO> actualizarItem(
            @PathVariable Long id, 
            @Valid @RequestBody PedidoItemDTO itemDTO) {
        return pedidoItemService.actualizarItem(id, itemDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar un item de un pedido
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id) {
        if (pedidoItemService.eliminarItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Obtener estadísticas de ventas de un producto
     */
    @GetMapping("/estadisticas/producto/{productoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> obtenerEstadisticasProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(pedidoItemService.obtenerEstadisticasProducto(productoId));
    }

    /**
     * Obtener productos más vendidos
     */
    @GetMapping("/estadisticas/mas-vendidos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Object[]>> obtenerProductosMasVendidos() {
        List<Object[]> productos = pedidoItemService.obtenerProductosMasVendidos();
        return ResponseEntity.ok(productos);
    }
}