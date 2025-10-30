package com.api.e_commerce.service;

import com.api.e_commerce.dto.PedidoItemDTO;
import com.api.e_commerce.model.PedidoItem;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.Producto;
import com.api.e_commerce.repository.PedidoItemRepository;
import com.api.e_commerce.repository.PedidoRepository;
import com.api.e_commerce.repository.ProductoRepository;
import com.api.e_commerce.exception.PedidoNotFoundException;
import com.api.e_commerce.exception.ProductoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para gestionar items de pedidos
 */
@Service
@Transactional
public class PedidoItemService {
    
    @Autowired
    private PedidoItemRepository pedidoItemRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * Obtiene todos los items de un pedido específico
     */
    public List<PedidoItemDTO> obtenerItemsPorPedido(Long pedidoId) {
        return pedidoItemRepository.findByPedidoId(pedidoId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene un item por su ID
     */
    public Optional<PedidoItemDTO> obtenerItemPorId(Long id) {
        return pedidoItemRepository.findById(id)
                .map(this::convertirADTO);
    }
    
    /**
     * Crea un nuevo item en un pedido
     */
    public PedidoItemDTO crearItem(PedidoItemDTO itemDTO) {
        // Validar que existan el pedido y el producto
        Pedido pedido = pedidoRepository.findById(itemDTO.getPedidoId())
                .orElseThrow(() -> new PedidoNotFoundException("Pedido no encontrado con ID: " + itemDTO.getPedidoId()));
        
        Producto producto = productoRepository.findById(itemDTO.getProductoId())
                .orElseThrow(() -> new ProductoNotFoundException("Producto no encontrado con ID: " + itemDTO.getProductoId()));
        
        // Crear el item
        PedidoItem item = new PedidoItem();
        item.setCantidad(itemDTO.getCantidad());
        item.setPrecioUnitario(itemDTO.getPrecioUnitario() != null ? itemDTO.getPrecioUnitario() : producto.getPrecio());
        item.setPedido(pedido);
        item.setProducto(producto);
        
        // El subtotal se calcula automáticamente en @PrePersist
        PedidoItem itemGuardado = pedidoItemRepository.save(item);
        
        // Recalcular total del pedido
        pedido.calcularTotal();
        pedidoRepository.save(pedido);
        
        return convertirADTO(itemGuardado);
    }
    
    /**
     * Actualiza un item existente
     */
    public Optional<PedidoItemDTO> actualizarItem(Long id, PedidoItemDTO itemDTO) {
        return pedidoItemRepository.findById(id)
                .map(item -> {
                    if (itemDTO.getCantidad() != null) {
                        item.setCantidad(itemDTO.getCantidad());
                    }
                    if (itemDTO.getPrecioUnitario() != null) {
                        item.setPrecioUnitario(itemDTO.getPrecioUnitario());
                    }
                    
                    PedidoItem itemActualizado = pedidoItemRepository.save(item);
                    
                    // Recalcular total del pedido
                    item.getPedido().calcularTotal();
                    pedidoRepository.save(item.getPedido());
                    
                    return convertirADTO(itemActualizado);
                });
    }
    
    /**
     * Elimina un item de un pedido
     */
    public boolean eliminarItem(Long id) {
        return pedidoItemRepository.findById(id)
                .map(item -> {
                    Pedido pedido = item.getPedido();
                    pedidoItemRepository.delete(item);
                    
                    // Recalcular total del pedido
                    pedido.calcularTotal();
                    pedidoRepository.save(pedido);
                    
                    return true;
                })
                .orElse(false);
    }
    
    /**
     * Obtiene estadísticas de un producto
     */
    public Map<String, Object> obtenerEstadisticasProducto(Long productoId) {
        Map<String, Object> estadisticas = new HashMap<>();
        
        Integer totalVendido = pedidoItemRepository.contarVentasProducto(productoId);
        Double ingresos = pedidoItemRepository.calcularIngresosPorProducto(productoId);
        
        estadisticas.put("productoId", productoId);
        estadisticas.put("totalVendido", totalVendido);
        estadisticas.put("ingresosTotales", ingresos);
        
        return estadisticas;
    }
    
    /**
     * Obtiene los productos más vendidos
     */
    public List<Object[]> obtenerProductosMasVendidos() {
        return pedidoItemRepository.findProductosMasVendidos();
    }
    
    /**
     * Convierte un PedidoItem a PedidoItemDTO
     */
    private PedidoItemDTO convertirADTO(PedidoItem item) {
        PedidoItemDTO dto = new PedidoItemDTO();
        dto.setId(item.getId());
        dto.setCantidad(item.getCantidad());
        dto.setPrecioUnitario(item.getPrecioUnitario());
        dto.setSubtotal(item.getSubtotal());
        
        if (item.getPedido() != null) {
            dto.setPedidoId(item.getPedido().getId());
        }
        
        if (item.getProducto() != null) {
            dto.setProductoId(item.getProducto().getId());
            dto.setNombreObra(item.getProducto().getNombreObra());
            dto.setImagen(item.getProducto().getImagen());
            dto.setArtista(item.getProducto().getArtista());
        }
        
        return dto;
    }
}