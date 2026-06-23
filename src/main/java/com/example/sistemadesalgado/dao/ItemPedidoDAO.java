package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.ItemPedido;

import java.util.List;

public interface ItemPedidoDAO {

    ItemPedido save(ItemPedido itemPedido);

    ItemPedido findById(Long id);

    void deleteById(Long id);
}
