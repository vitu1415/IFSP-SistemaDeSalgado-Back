package com.example.sistemadesalgado.dao.impl;

import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.Salgado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class SalgadoDAOImpl implements SalgadoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Salgado save(Salgado salgado) {
        entityManager.persist(salgado);
        return salgado;
    }

    @Override
    public Optional<Salgado> findById(Long id) {
        Salgado salgado = entityManager.find(Salgado.class, id);
        return Optional.ofNullable(salgado);
    }

    @Override
    public Optional<Salgado> findBySabor(String sabor) {
        TypedQuery<Salgado> query = entityManager.createQuery(
                "SELECT s FROM Salgado s WHERE s.sabor = :sabor", Salgado.class);
        query.setParameter("sabor", sabor);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Salgado> findAll() {
        TypedQuery<Salgado> query = entityManager.createQuery(
                "SELECT s FROM Salgado s", Salgado.class);
        return query.getResultList();
    }

    @Override
    public List<Salgado> findByEstoqueGreaterThan(Integer estoque) {
        TypedQuery<Salgado> query = entityManager.createQuery(
                "SELECT s FROM Salgado s WHERE s.estoque > :estoque", Salgado.class);
        query.setParameter("estoque", estoque);
        return query.getResultList();
    }

    @Override
    public List<Salgado> findByEstoqueGreaterThanOrderByPrecoAsc(Integer estoque) {
        TypedQuery<Salgado> query = entityManager.createQuery(
                "SELECT s FROM Salgado s WHERE s.estoque > :estoque ORDER BY s.preco ASC", Salgado.class);
        query.setParameter("estoque", estoque);
        return query.getResultList();
    }

    @Override
    public List<Salgado> findBySaborContainingIgnoreCase(String sabor) {
        TypedQuery<Salgado> query = entityManager.createQuery(
                "SELECT s FROM Salgado s WHERE LOWER(s.sabor) LIKE LOWER(:sabor)", Salgado.class);
        query.setParameter("sabor", "%" + sabor + "%");
        return query.getResultList();
    }

    @Override
    public boolean existsBySabor(String sabor) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM Salgado s WHERE s.sabor = :sabor", Long.class);
        query.setParameter("sabor", sabor);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional
    public Salgado update(Salgado salgado) {
        return entityManager.merge(salgado);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Salgado salgado = entityManager.find(Salgado.class, id);
        if (salgado != null) {
            entityManager.remove(salgado);
        }
    }
}
