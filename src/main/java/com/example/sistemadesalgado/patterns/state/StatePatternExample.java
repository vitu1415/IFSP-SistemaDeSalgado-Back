package com.example.sistemadesalgado.patterns.state;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StatePatternExample implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("=== INICIANDO EXEMPLOS DO STATE PATTERN ===\n");

        // Exemplo 1: Fluxo normal de pedido
        exemploFluxoNormalPedido();

        // Exemplo 2: Cancelamento de pedido criado
        exemploCancelamentoPedidoCriado();

        // Exemplo 3: Cancelamento durante preparação
        exemploCancelamentoDurantePreparacao();

        // Exemplo 4: Tentativas de transições inválidas
        exemploTransicoesInvalidas();

        // Exemplo 5: Demonstração de múltiplos pedidos com estados diferentes
        exemploMultiplosPedidos();

        System.out.println("=== EXEMPLOS DO STATE PATTERN CONCLUÍDOS ===");
    }

    private void exemploFluxoNormalPedido() {
        System.out.println("### EXEMPLO 1: FLUXO NORMAL DE PEDIDO ###\n");

        PedidoContext pedido = new PedidoContext();
        
        System.out.println("Estado inicial: " + pedido.getEstadoAtual());
        System.out.println("O pedido foi criado e está aguardando preparação.\n");

        // Preparar pedido
        pedido.preparar();

        System.out.println("O pedido está sendo preparado na cozinha.\n");

        // Entregar pedido
        pedido.entregar();

        System.out.println("O pedido foi entregue ao cliente.\n");

        System.out.println("Estado final: " + pedido.getEstadoAtual());
        System.out.println("Fluxo completo: CRIADO -> PREPARANDO -> ENTREGUE");

        System.out.println("\n### FIM DO EXEMPLO 1 ###\n");
    }

    private void exemploCancelamentoPedidoCriado() {
        System.out.println("### EXEMPLO 2: CANCELAMENTO DE PEDIDO CRIADO ###\n");

        PedidoContext pedido = new PedidoContext();
        
        System.out.println("Estado inicial: " + pedido.getEstadoAtual());
        System.out.println("O pedido foi criado, mas o cliente desistiu.\n");

        // Cancelar pedido
        pedido.cancelar();

        System.out.println("O pedido foi cancelado antes de iniciar a preparação.\n");

        System.out.println("Estado final: " + pedido.getEstadoAtual());
        System.out.println("Fluxo: CRIADO -> CANCELADO");

        System.out.println("\n### FIM DO EXEMPLO 2 ###\n");
    }

    private void exemploCancelamentoDurantePreparacao() {
        System.out.println("### EXEMPLO 3: CANCELAMENTO DURANTE PREPARAÇÃO ###\n");

        PedidoContext pedido = new PedidoContext();
        
        System.out.println("Estado inicial: " + pedido.getEstadoAtual());
        
        // Preparar pedido
        pedido.preparar();
        
        System.out.println("O pedido está sendo preparado, mas ocorreu um problema.\n");

        // Cancelar pedido durante preparação
        pedido.cancelar();

        System.out.println("O pedido foi cancelado durante a preparação.\n");

        System.out.println("Estado final: " + pedido.getEstadoAtual());
        System.out.println("Fluxo: CRIADO -> PREPARANDO -> CANCELADO");

        System.out.println("\n### FIM DO EXEMPLO 3 ###\n");
    }

    private void exemploTransicoesInvalidas() {
        System.out.println("### EXEMPLO 4: TENTATIVAS DE TRANSIÇÕES INVÁLIDAS ###\n");

        // Tentar entregar pedido criado
        System.out.println("--- Teste 1: Tentar entregar pedido criado ---");
        PedidoContext pedido1 = new PedidoContext();
        System.out.println("Estado atual: " + pedido1.getEstadoAtual());
        
        try {
            pedido1.entregar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        // Tentar preparar pedido já preparando
        System.out.println("--- Teste 2: Tentar preparar pedido já em preparação ---");
        PedidoContext pedido2 = new PedidoContext();
        pedido2.preparar();
        System.out.println("Estado atual: " + pedido2.getEstadoAtual());
        
        try {
            pedido2.preparar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        // Tentar cancelar pedido entregue
        System.out.println("--- Teste 3: Tentar cancelar pedido entregue ---");
        PedidoContext pedido3 = new PedidoContext();
        pedido3.preparar();
        pedido3.entregar();
        System.out.println("Estado atual: " + pedido3.getEstadoAtual());
        
        try {
            pedido3.cancelar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        // Tentar preparar pedido cancelado
        System.out.println("--- Teste 4: Tentar preparar pedido cancelado ---");
        PedidoContext pedido4 = new PedidoContext();
        pedido4.cancelar();
        System.out.println("Estado atual: " + pedido4.getEstadoAtual());
        
        try {
            pedido4.preparar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        // Tentar entregar pedido cancelado
        System.out.println("--- Teste 5: Tentar entregar pedido cancelado ---");
        PedidoContext pedido5 = new PedidoContext();
        pedido5.cancelar();
        System.out.println("Estado atual: " + pedido5.getEstadoAtual());
        
        try {
            pedido5.entregar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        // Tentar entregar pedido já entregue
        System.out.println("--- Teste 6: Tentar entregar pedido já entregue ---");
        PedidoContext pedido6 = new PedidoContext();
        pedido6.preparar();
        pedido6.entregar();
        System.out.println("Estado atual: " + pedido6.getEstadoAtual());
        
        try {
            pedido6.entregar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println();

        System.out.println("### FIM DO EXEMPLO 4 ###\n");
    }

    private void exemploMultiplosPedidos() {
        System.out.println("### EXEMPLO 5: MÚLTIPLOS PEDIDOS COM ESTADOS DIFERENTES ###\n");

        // Pedido 1: Em preparação
        PedidoContext pedido1 = new PedidoContext();
        pedido1.preparar();
        System.out.println("Pedido 1 - Estado: " + pedido1.getEstadoAtual());

        // Pedido 2: Criado
        PedidoContext pedido2 = new PedidoContext();
        System.out.println("Pedido 2 - Estado: " + pedido2.getEstadoAtual());

        // Pedido 3: Entregue
        PedidoContext pedido3 = new PedidoContext();
        pedido3.preparar();
        pedido3.entregar();
        System.out.println("Pedido 3 - Estado: " + pedido3.getEstadoAtual());

        // Pedido 4: Cancelado
        PedidoContext pedido4 = new PedidoContext();
        pedido4.cancelar();
        System.out.println("Pedido 4 - Estado: " + pedido4.getEstadoAtual());

        System.out.println("\n--- Continuando com cada pedido ---\n");

        // Continuar pedido 1
        System.out.println("Continuando Pedido 1:");
        pedido1.entregar();
        System.out.println("Pedido 1 - Novo Estado: " + pedido1.getEstadoAtual() + "\n");

        // Continuar pedido 2
        System.out.println("Continuando Pedido 2:");
        pedido2.preparar();
        System.out.println("Pedido 2 - Novo Estado: " + pedido2.getEstadoAtual());
        pedido2.entregar();
        System.out.println("Pedido 2 - Novo Estado: " + pedido2.getEstadoAtual() + "\n");

        // Tentar continuar pedido 3
        System.out.println("Tentando continuar Pedido 3 (já entregue):");
        try {
            pedido3.preparar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println("Pedido 3 - Estado permanece: " + pedido3.getEstadoAtual() + "\n");

        // Tentar continuar pedido 4
        System.out.println("Tentando continuar Pedido 4 (cancelado):");
        try {
            pedido4.preparar();
        } catch (IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
        System.out.println("Pedido 4 - Estado permanece: " + pedido4.getEstadoAtual() + "\n");

        System.out.println("### FIM DO EXEMPLO 5 ###\n");
    }
}
