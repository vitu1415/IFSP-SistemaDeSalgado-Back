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
    public List<ItemPedido> findByPedidoId(Long pedidoId) {
        TypedQuery<ItemPedido> query = entityManager.createQuery(
                "SELECT ip FROM ItemPedido ip WHERE ip.pedido.id = :pedidoId", ItemPedido.class);
        query.setParameter("pedidoId", pedidoId);
        return query.getResultList();
    }

    @Override
    public List<ItemPedido> findBySalgadoEstoqueId(Long salgadoEstoqueId) {
        TypedQuery<ItemPedido> query = entityManager.createQuery(
                "SELECT ip FROM ItemPedido ip WHERE ip.salgadoEstoque.id = :salgadoEstoqueId", ItemPedido.class);
        query.setParameter("salgadoEstoqueId", salgadoEstoqueId);
        return query.getResultList();
    }

    @Override
    public List<ItemPedido> findByPedidoIds(List<Long> pedidoIds) {
        TypedQuery<ItemPedido> query = entityManager.createQuery(
                "SELECT ip FROM ItemPedido ip WHERE ip.pedido.id IN :pedidoIds", ItemPedido.class);
        query.setParameter("pedidoIds", pedidoIds);
        return query.getResultList();
    }

    @Override
    public List<Object[]> findSaborMaisPedidoByCliente(Long clienteId) {
        TypedQuery<Object[]> query = entityManager.createQuery(
                "SELECT ip.sabor, SUM(ip.quantidade) FROM ItemPedido ip WHERE ip.pedido.cliente.id = :clienteId GROUP BY ip.sabor", Object[].class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ItemPedido itemPedido = entityManager.find(ItemPedido.class, id);
        if (itemPedido != null) {
            entityManager.remove(itemPedido);
        }
    }

    @Override
    @Transactional
    public void delete(ItemPedido itemPedido) {
        entityManager.remove(itemPedido);
    }
}
