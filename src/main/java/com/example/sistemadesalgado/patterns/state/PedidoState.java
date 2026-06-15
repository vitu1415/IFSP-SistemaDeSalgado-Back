package com.example.sistemadesalgado.patterns.state;

public interface PedidoState {

    void preparar(PedidoContext context);

    void entregar(PedidoContext context);

    void cancelar(PedidoContext context);

    String getNomeEstado();
}
