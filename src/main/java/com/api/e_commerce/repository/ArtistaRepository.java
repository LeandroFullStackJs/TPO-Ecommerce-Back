package com.api.e_commerce.repository;

import com.api.e_commerce.model.Artista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    
    // Buscar artistas activos
    List<Artista> findByActivoTrue();
    
    // Buscar por nombre (case insensitive)
    @Query("SELECT a FROM Artista a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND a.activo = true")
    List<Artista> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    // Buscar por email
    Optional<Artista> findByEmailAndActivoTrue(String email);
    
    // Verificar si existe email (para validación de unicidad)
    boolean existsByEmail(String email);
    
    // Verificar si existe email excluyendo un ID (para updates)
    boolean existsByEmailAndIdNot(String email, Long id);
}