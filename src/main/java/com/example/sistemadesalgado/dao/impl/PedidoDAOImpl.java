package com.example.sistemadesalgado.dao.impl;

import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PedidoDAOImpl implements PedidoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Pedido save(Pedido pedido) {
        entityManager.persist(pedido);
        return pedido;
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        Pedido pedido = entityManager.find(Pedido.class, id);
        return Optional.ofNullable(pedido);
    }

    @Override
    public List<Pedido> findByClienteId(Long clienteId) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId", Pedido.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dataCriacao DESC", Pedido.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findByClienteIdAndStatus(Long clienteId, StatusPedido status) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status = :status", Pedido.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findByStatus(StatusPedido status) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.status = :status", Pedido.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findByDataCriacaoBetween(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.dataCriacao BETWEEN :startDate AND :endDate", Pedido.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findHistoricoPedidosByClienteAndData(Long clienteId, LocalDateTime startDate) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataCriacao >= :startDate ORDER BY p.dataCriacao DESC", Pedido.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("startDate", startDate);
        return query.getResultList();
    }

    @Override
    public List<Pedido> findPedidosByClienteAndStatuses(Long clienteId, List<StatusPedido> statuses) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status IN :statuses ORDER BY p.dataCriacao DESC", Pedido.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("statuses", statuses);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Pedido update(Pedido pedido) {
        return entityManager.merge(pedido);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Pedido pedido = entityManager.find(Pedido.class, id);
        if (pedido != null) {
            entityManager.remove(pedido);
        }
    }

    @Override
    public boolean existsById(Long id) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Pedido p WHERE p.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() > 0;
    }
}
