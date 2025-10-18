package com.api.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.e_commerce.model.Categoria;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
}