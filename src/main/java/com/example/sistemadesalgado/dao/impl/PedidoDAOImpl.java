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
    public List<Pedido> findAllOrderByDataCriacaoDesc() {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p ORDER BY p.dataCriacao DESC", Pedido.class);
        return query.getResultList();
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
    public List<Pedido> findHistoricoPedidosByClienteAndData(Long clienteId, LocalDateTime startDate) {
        TypedQuery<Pedido> query = entityManager.createQuery(
                "SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataCriacao >= :startDate ORDER BY p.dataCriacao DESC", Pedido.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("startDate", startDate);
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
}
