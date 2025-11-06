package com.api.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.api.e_commerce.model.Pedido;
import com.api.e_commerce.model.Usuario;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.usuario = :usuario")
    List<Pedido> findByUsuario(Usuario usuario);
    
    List<Pedido> findByEstado(String estado);

    List<Pedido> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario")
    List<Pedido> findAllWithUsuario();
}