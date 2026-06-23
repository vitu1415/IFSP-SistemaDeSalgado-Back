package com.example.sistemadesalgado.dao.impl;

import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
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
    public SalgadoEstoque save(SalgadoEstoque salgadoEstoque) {
        entityManager.persist(salgadoEstoque);
        return salgadoEstoque;
    }

    @Override
    public Optional<SalgadoEstoque> findById(Long id) {
        SalgadoEstoque salgadoEstoque = entityManager.find(SalgadoEstoque.class, id);
        return Optional.ofNullable(salgadoEstoque);
    }

    @Override
    public Optional<SalgadoEstoque> findBySabor(String sabor) {
        TypedQuery<SalgadoEstoque> query = entityManager.createQuery(
                "SELECT s FROM SalgadoEstoque s WHERE s.sabor = :sabor", SalgadoEstoque.class);
        query.setParameter("sabor", sabor);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsBySabor(String sabor) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(s) FROM SalgadoEstoque s WHERE s.sabor = :sabor", Long.class);
        query.setParameter("sabor", sabor);
        return query.getSingleResult() > 0;
    }

    @Override
    public List<SalgadoEstoque> findAll() {
        TypedQuery<SalgadoEstoque> query = entityManager.createQuery(
                "SELECT s FROM SalgadoEstoque s", SalgadoEstoque.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public SalgadoEstoque update(SalgadoEstoque salgadoEstoque) {
        return entityManager.merge(salgadoEstoque);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        SalgadoEstoque salgadoEstoque = entityManager.find(SalgadoEstoque.class, id);
        if (salgadoEstoque != null) {
            entityManager.remove(salgadoEstoque);
        }
    }
}
