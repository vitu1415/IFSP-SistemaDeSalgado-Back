package com.example.sistemadesalgado.repository;

import com.example.sistemadesalgado.model.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoId(Long pedidoId);

    List<ItemPedido> findBySalgadoId(Long salgadoId);

    @Query("SELECT ip FROM ItemPedido ip WHERE ip.pedido.id IN :pedidoIds")
    List<ItemPedido> findByPedidoIds(@Param("pedidoIds") List<Long> pedidoIds);

    @Query("SELECT ip.sabor, SUM(ip.quantidade) FROM ItemPedido ip WHERE ip.pedido.cliente.id = :clienteId GROUP BY ip.sabor")
    List<Object[]> findSaborMaisPedidoByCliente(@Param("clienteId") Long clienteId);
}
