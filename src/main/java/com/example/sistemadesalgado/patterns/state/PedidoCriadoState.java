package com.example.sistemadesalgado.patterns.state;

import org.springframework.stereotype.Component;

@Component
public class PedidoCriadoState implements PedidoState {

    @Override
    public void preparar(PedidoContext context) {
        System.out.println(">>> PedidoCriadoState.preparar() - Iniciando preparação do pedido");
        context.setEstado(new PedidoPreparandoState());
        System.out.println("Estado alterado para: " + context.getEstado().getNomeEstado());
    }

    @Override
    public void entregar(PedidoContext context) {
        throw new IllegalStateException("Não é possível entregar um pedido que ainda não foi preparado. Estado atual: " + getNomeEstado());
    }

    @Override
    public void cancelar(PedidoContext context) {
        System.out.println(">>> PedidoCriadoState.cancelar() - Cancelando pedido");
        context.setEstado(new PedidoCanceladoState());
        System.out.println("Estado alterado para: " + context.getEstado().getNomeEstado());
    }

    @Override
    public String getNomeEstado() {
        return "CRIADO";
    }
}
