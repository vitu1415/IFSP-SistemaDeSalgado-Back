package com.example.sistemadesalgado.patterns.state;

import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;

public class PedidoContext {

    private final Pedido pedido;

    public PedidoContext(Pedido pedido) {
        this.pedido = pedido;
    }

    public boolean preparar() {
        PedidoState estado = fromStatus(pedido.getStatus());
        return estado.preparar(pedido);
    }

    public boolean entregar() {
        PedidoState estado = fromStatus(pedido.getStatus());
        return estado.entregar(pedido);
    }

    public boolean cancelar() {
        PedidoState estado = fromStatus(pedido.getStatus());
        return estado.cancelar(pedido);
    }

    private PedidoState fromStatus(StatusPedido status) {
        return switch (status) {
            case CRIADO -> new PedidoCriadoState();
            case CONFIRMADO -> new PedidoPreparandoState();
            case ESTORNADO -> new PedidoCanceladoState();
        };
    }
}
