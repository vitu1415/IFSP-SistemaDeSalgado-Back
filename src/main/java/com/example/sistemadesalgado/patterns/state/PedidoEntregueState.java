package com.example.sistemadesalgado.patterns.state;

import org.springframework.stereotype.Component;

@Component
public class PedidoEntregueState implements PedidoState {

    @Override
    public void preparar(PedidoContext context) {
        throw new IllegalStateException("Não é possível preparar um pedido já entregue. Estado atual: " + getNomeEstado());
    }

    @Override
    public void entregar(PedidoContext context) {
        throw new IllegalStateException("O pedido já foi entregue. Estado atual: " + getNomeEstado());
    }

    @Override
    public void cancelar(PedidoContext context) {
        throw new IllegalStateException("Não é possível cancelar um pedido já entregue. Estado atual: " + getNomeEstado());
    }

    @Override
    public String getNomeEstado() {
        return "ENTREGUE";
    }
}
