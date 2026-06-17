package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.ItemPedido;

import java.util.List;

public interface ItemPedidoDAO {

    ItemPedido save(ItemPedido itemPedido);

    ItemPedido findById(Long id);

    List<ItemPedido> findByPedidoId(Long pedidoId);

    List<ItemPedido> findBySalgadoEstoqueId(Long salgadoEstoqueId);

    List<ItemPedido> findByPedidoIds(List<Long> pedidoIds);

    List<Object[]> findSaborMaisPedidoByCliente(Long clienteId);

    void deleteById(Long id);

    void delete(ItemPedido itemPedido);
}
