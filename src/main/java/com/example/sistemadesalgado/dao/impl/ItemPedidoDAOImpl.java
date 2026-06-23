package com.example.sistemadesalgado.dao.impl;

import com.example.sistemadesalgado.dao.ItemPedidoDAO;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ItemPedidoDAOImpl implements ItemPedidoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public ItemPedido save(ItemPedido itemPedido) {
        entityManager.persist(itemPedido);
        return itemPedido;
    }

    @Override
    public ItemPedido findById(Long id) {
        return entityManager.find(ItemPedido.class, id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ItemPedido itemPedido = entityManager.find(ItemPedido.class, id);
        if (itemPedido != null) {
            entityManager.remove(itemPedido);
        }
    }
}
