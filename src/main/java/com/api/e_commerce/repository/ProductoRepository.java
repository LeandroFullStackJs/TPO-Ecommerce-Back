package com.api.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.e_commerce.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Métodos CRUD heredados de JpaRepository
    // List<Producto> findAll();
    // Optional<Producto> findById(Long id);
    // Producto save(Producto producto);
    // void deleteById(Long id);

    // Consultas personalizadas para el frontend
    List<Producto> findByNombreObraContaining(String nombreObra);
    
    // Buscar productos activos
    List<Producto> findByActivoTrue();
    
    // Buscar productos destacados
    List<Producto> findByDestacadoTrue();
    
    // Buscar por categoría
    @Query("SELECT p FROM Producto p JOIN p.categorias c WHERE c.id = :categoriaId")
    List<Producto> findByCategorias_Id(@Param("categoriaId") Long categoriaId);
    
    // Búsqueda por nombre o descripción
    List<Producto> findByNombreObraContainingIgnoreCaseOrDescripcionContainingIgnoreCase(String nombreObra, String descripcion);
    
    // Buscar por rango de precio
    List<Producto> findByPrecioBetween(Double minPrecio, Double maxPrecio);
    
    // Buscar por stock mayor a
    List<Producto> findByStockGreaterThan(Integer stock);
    
    // Buscar por stock menor a
    List<Producto> findByStockLessThan(Integer stock);
    
    // Buscar por artista
    List<Producto> findByArtistaContainingIgnoreCase(String artista);
    
    // Buscar por técnica
    List<Producto> findByTecnicaContainingIgnoreCase(String tecnica);
    
    // Buscar por estilo
    List<Producto> findByEstiloContainingIgnoreCase(String estilo);
    
    // Buscar productos con stock disponible
    List<Producto> findByStockGreaterThanAndActivoTrue(Integer stock);
    
    // Buscar productos por múltiples criterios
    @Query("SELECT p FROM Producto p WHERE " +
           "(:nombreObra IS NULL OR LOWER(p.nombreObra) LIKE LOWER(CONCAT('%', :nombreObra, '%'))) AND " +
           "(:minPrecio IS NULL OR p.precio >= :minPrecio) AND " +
           "(:maxPrecio IS NULL OR p.precio <= :maxPrecio) AND " +
           "(:artista IS NULL OR LOWER(p.artista) LIKE LOWER(CONCAT('%', :artista, '%'))) AND " +
           "(:tecnica IS NULL OR LOWER(p.tecnica) LIKE LOWER(CONCAT('%', :tecnica, '%'))) AND " +
           "(:activo IS NULL OR p.activo = :activo)")
    List<Producto> findProductosConFiltros(@Param("nombreObra") String nombreObra,
                                         @Param("minPrecio") Double minPrecio,
                                         @Param("maxPrecio") Double maxPrecio,
                                         @Param("artista") String artista,
                                         @Param("tecnica") String tecnica,
                                         @Param("activo") Boolean activo);
}