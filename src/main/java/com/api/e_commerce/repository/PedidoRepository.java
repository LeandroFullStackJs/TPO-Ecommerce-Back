package com.api.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.Usuario;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
    List<Pedido> findByEstado(String estado);
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario")
    List<Pedido> findAllWithUsuario();
}