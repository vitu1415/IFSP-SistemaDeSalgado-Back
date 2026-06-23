package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(Long id);

    List<Pedido> findAllOrderByDataCriacaoDesc();

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findHistoricoPedidosByClienteAndData(Long clienteId, LocalDateTime startDate);

    Pedido update(Pedido pedido);

    void deleteById(Long id);

}
