package com.example.sistemadesalgado.repository;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteId(Long clienteId);

    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);

    List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByDataCriacaoBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataCriacao >= :startDate ORDER BY p.dataCriacao DESC")
    List<Pedido> findHistoricoPedidosByClienteAndData(@Param("clienteId") Long clienteId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status IN :statuses ORDER BY p.dataCriacao DESC")
    List<Pedido> findPedidosByClienteAndStatuses(@Param("clienteId") Long clienteId, @Param("statuses") List<StatusPedido> statuses);
}
