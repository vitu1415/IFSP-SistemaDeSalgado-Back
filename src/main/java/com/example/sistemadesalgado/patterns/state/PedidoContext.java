package com.example.sistemadesalgado.patterns.state;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PedidoContext {

    private PedidoState estado;

    public PedidoContext() {
        this.estado = new PedidoCriadoState();
    }

    public void preparar() {
        System.out.println("\n=== AÇÃO: PREPARAR PEDIDO ===");
        System.out.println("Estado atual: " + estado.getNomeEstado());
        estado.preparar(this);
        System.out.println("Novo estado: " + estado.getNomeEstado());
        System.out.println("=== FIM DA AÇÃO ===\n");
    }

    public void entregar() {
        System.out.println("\n=== AÇÃO: ENTREGAR PEDIDO ===");
        System.out.println("Estado atual: " + estado.getNomeEstado());
        estado.entregar(this);
        System.out.println("Novo estado: " + estado.getNomeEstado());
        System.out.println("=== FIM DA AÇÃO ===\n");
    }

    public void cancelar() {
        System.out.println("\n=== AÇÃO: CANCELAR PEDIDO ===");
        System.out.println("Estado atual: " + estado.getNomeEstado());
        estado.cancelar(this);
        System.out.println("Novo estado: " + estado.getNomeEstado());
        System.out.println("=== FIM DA AÇÃO ===\n");
    }

    public String getEstadoAtual() {
        return estado.getNomeEstado();
    }
}
