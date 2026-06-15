package com.example.sistemadesalgado.patterns.state;

import org.springframework.stereotype.Component;

@Component
public class PedidoPreparandoState implements PedidoState {

    @Override
    public void preparar(PedidoContext context) {
        throw new IllegalStateException("O pedido já está sendo preparado. Estado atual: " + getNomeEstado());
    }

    @Override
    public void entregar(PedidoContext context) {
        System.out.println(">>> PedidoPreparandoState.entregar() - Entregando pedido");
        context.setEstado(new PedidoEntregueState());
        System.out.println("Estado alterado para: " + context.getEstado().getNomeEstado());
    }

    @Override
    public void cancelar(PedidoContext context) {
        System.out.println(">>> PedidoPreparandoState.cancelar() - Cancelando pedido em preparação");
        context.setEstado(new PedidoCanceladoState());
        System.out.println("Estado alterado para: " + context.getEstado().getNomeEstado());
    }

    @Override
    public String getNomeEstado() {
        return "PREPARANDO";
    }
}
