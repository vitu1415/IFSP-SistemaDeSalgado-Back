package com.example.sistemadesalgado.patterns.state;

import org.springframework.stereotype.Component;

@Component
public class PedidoCanceladoState implements PedidoState {

    @Override
    public void preparar(PedidoContext context) {
        throw new IllegalStateException("Não é possível preparar um pedido cancelado. Estado atual: " + getNomeEstado());
    }

    @Override
    public void entregar(PedidoContext context) {
        throw new IllegalStateException("Não é possível entregar um pedido cancelado. Estado atual: " + getNomeEstado());
    }

    @Override
    public void cancelar(PedidoContext context) {
        throw new IllegalStateException("O pedido já está cancelado. Estado atual: " + getNomeEstado());
    }

    @Override
    public String getNomeEstado() {
        return "CANCELADO";
    }
}
