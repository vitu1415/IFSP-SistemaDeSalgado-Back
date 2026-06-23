package com.example.sistemadesalgado.patterns.state;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

public interface PedidoState {

    boolean preparar(Pedido pedido);

    boolean entregar(Pedido pedido);

    boolean cancelar(Pedido pedido);

    StatusPedido getStatus();
}
