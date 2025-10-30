package com.api.e_commerce.repository;

import com.api.e_commerce.model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad PedidoItem
 */
@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    
    /**
     * Encuentra todos los items de un pedido específico
     */
    List<PedidoItem> findByPedidoId(Long pedidoId);
    
    /**
     * Encuentra todos los items que contienen un producto específico
     */
    List<PedidoItem> findByProductoId(Long productoId);
    
    /**
     * Calcula el total de ventas de un producto específico
     */
    @Query("SELECT COALESCE(SUM(pi.cantidad), 0) FROM PedidoItem pi WHERE pi.producto.id = :productoId")
    Integer contarVentasProducto(@Param("productoId") Long productoId);
    
    /**
     * Encuentra los productos más vendidos
     */
    @Query("SELECT pi.producto.id, SUM(pi.cantidad) as total " +
           "FROM PedidoItem pi " +
           "GROUP BY pi.producto.id " +
           "ORDER BY total DESC")
    List<Object[]> findProductosMasVendidos();
    
    /**
     * Calcula el ingreso total por producto
     */
    @Query("SELECT COALESCE(SUM(pi.subtotal), 0.0) FROM PedidoItem pi WHERE pi.producto.id = :productoId")
    Double calcularIngresosPorProducto(@Param("productoId") Long productoId);
}