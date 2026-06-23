package com.example.sistemadesalgado.patterns.state;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

public class PedidoCriadoState implements PedidoState {

    @Override
    public boolean preparar(Pedido pedido) {
        pedido.setStatus(StatusPedido.CONFIRMADO);
        return true;
    }

    @Override
    public boolean entregar(Pedido pedido) {
        return false;
    }

    @Override
    public boolean cancelar(Pedido pedido) {
        pedido.setStatus(StatusPedido.ESTORNADO);
        return true;
    }

    @Override
    public StatusPedido getStatus() {
        return StatusPedido.CRIADO;
    }
}
