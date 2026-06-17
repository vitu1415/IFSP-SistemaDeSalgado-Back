package com.example.sistemadesalgado.dao.impl;

import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class MovimentoDAOImpl implements MovimentoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Movimento save(Movimento movimento) {
        entityManager.persist(movimento);
        return movimento;
    }

    @Override
    public Optional<Movimento> findById(Long id) {
        Movimento movimento = entityManager.find(Movimento.class, id);
        return Optional.ofNullable(movimento);
    }

    @Override
    public List<Movimento> findByClienteId(Long clienteId) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.cliente.id = :clienteId", Movimento.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findByClienteIdOrderByDataHoraDesc(Long clienteId) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.cliente.id = :clienteId ORDER BY m.dataHora DESC", Movimento.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findByTipoMovimento(TipoMovimento tipoMovimento) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.tipoMovimento = :tipoMovimento", Movimento.class);
        query.setParameter("tipoMovimento", tipoMovimento);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findByClienteIdAndTipoMovimento(Long clienteId, TipoMovimento tipoMovimento) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.cliente.id = :clienteId AND m.tipoMovimento = :tipoMovimento", Movimento.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("tipoMovimento", tipoMovimento);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findByPedidoId(Long pedidoId) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.pedido.id = :pedidoId", Movimento.class);
        query.setParameter("pedidoId", pedidoId);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.dataHora BETWEEN :startDate AND :endDate", Movimento.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    @Override
    public List<Movimento> findHistoricoMovimentosByClienteAndData(Long clienteId, LocalDateTime startDate) {
        TypedQuery<Movimento> query = entityManager.createQuery(
                "SELECT m FROM Movimento m WHERE m.cliente.id = :clienteId AND m.dataHora >= :startDate ORDER BY m.dataHora DESC", Movimento.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("startDate", startDate);
        return query.getResultList();
    }

    @Override
    public Double sumValorByClienteAndTipoMovimento(Long clienteId, TipoMovimento tipoMovimento) {
        TypedQuery<Double> query = entityManager.createQuery(
                "SELECT COALESCE(SUM(m.valor), 0) FROM Movimento m WHERE m.cliente.id = :clienteId AND m.tipoMovimento = :tipoMovimento", Double.class);
        query.setParameter("clienteId", clienteId);
        query.setParameter("tipoMovimento", tipoMovimento);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Movimento movimento = entityManager.find(Movimento.class, id);
        if (movimento != null) {
            entityManager.remove(movimento);
        }
    }

    @Override
    @Transactional
    public void delete(Movimento movimento) {
        entityManager.remove(movimento);
    }
}
