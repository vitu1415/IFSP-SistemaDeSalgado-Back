package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    Pedido save(Pedido pedido);

    Optional<Pedido> findById(Long id);

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);

    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByDataCriacaoBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Pedido> findHistoricoPedidosByClienteAndData(Long clienteId, LocalDateTime startDate);

    List<Pedido> findPedidosByClienteAndStatuses(Long clienteId, List<StatusPedido> statuses);

    Pedido update(Pedido pedido);

    void deleteById(Long id);

    boolean existsById(Long id);
}
