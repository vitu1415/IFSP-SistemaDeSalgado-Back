package com.example.sistemadesalgado.patterns.state;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

public class PedidoCanceladoState implements PedidoState {

    @Override
    public boolean preparar(Pedido pedido) {
        return false;
    }

    @Override
    public boolean entregar(Pedido pedido) {
        return false;
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        return false;
    }

    @Override
    public StatusPedido getStatus() {
        return StatusPedido.ESTORNADO;
    }
}
