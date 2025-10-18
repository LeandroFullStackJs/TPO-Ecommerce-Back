package com.api.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.api.e_commerce.model.Direccion;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    
}