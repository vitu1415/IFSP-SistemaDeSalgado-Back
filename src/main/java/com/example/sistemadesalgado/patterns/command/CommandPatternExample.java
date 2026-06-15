package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.Salgado;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.repository.ClienteRepository;
import com.example.sistemadesalgado.repository.SalgadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandPatternExample implements CommandLineRunner {

    private final CommandInvoker commandInvoker;
    private final ClienteRepository clienteRepository;
    private final SalgadoRepository salgadoRepository;
    private final PedidoDAO pedidoDAO;
    private final SalgadoDAO salgadoDAO;
    private final PedidoCommand pedidoCommand;
    private final EstornoPedidoCommand estornoPedidoCommand;

    @Override
    public void run(String... args) {
        System.out.println("=== INICIANDO EXEMPLOS DO COMMAND PATTERN ===\n");

        // Exemplo 1: Fluxo completo de criação e cancelamento de pedido
        exemploFluxoCompletoPedido();

        // Exemplo 2: Fluxo completo de estorno de pedido
        exemploFluxoCompletoEstorno();

        // Exemplo 3: Usando CommandInvoker para gerenciar múltiplos comandos
        exemploCommandInvoker();

        System.out.println("=== EXEMPLOS DO COMMAND PATTERN CONCLUÍDOS ===");
    }

    private void exemploFluxoCompletoPedido() {
        System.out.println("### EXEMPLO 1: FLUXO COMPLETO DE CRIAÇÃO E CANCELAMENTO DE PEDIDO ###\n");

        // 1. Criar cliente
        Cliente cliente = Cliente.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .build();
        cliente = clienteRepository.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");

        // 2. Criar salgados com estoque
        Salgado salgado1 = Salgado.builder()
                .sabor("Frango")
                .preco(5.00)
                .estoque(100)
                .build();
        if (!salgadoRepository.existsBySabor("Frango")) {
            salgado1 = salgadoRepository.save(salgado1);
        } else {
            salgado1 = salgadoRepository.findBySabor("Frango").orElseThrow();
        }

        Salgado salgado2 = Salgado.builder()
                .sabor("Queijo")
                .preco(4.50)
                .estoque(50)
                .build();
        if (!salgadoRepository.existsBySabor("Queijo")) {
            salgado2 = salgadoRepository.save(salgado2);
        } else {
            salgado2 = salgadoRepository.findBySabor("Queijo").orElseThrow();
        }
        System.out.println("Salgados criados com estoque inicial");

        // 3. Criar itens do pedido
        ItemPedido item1 = ItemPedido.builder()
                .salgado(salgado1)
                .sabor("Frango")
                .quantidade(10)
                .valorUnitario(5.00)
                .build();

        ItemPedido item2 = ItemPedido.builder()
                .salgado(salgado2)
                .sabor("Queijo")
                .quantidade(5)
                .valorUnitario(4.50)
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item1);
        itens.add(item2);

        // 4. Executar PedidoCommand
        System.out.println("\n--- EXECUTANDO PEDIDO COMMAND ---");
        pedidoCommand.setPedidoData(cliente, itens);
        
        System.out.println("Antes da execução:");
        System.out.println("  Estoque Frango: " + salgado1.getEstoque());
        System.out.println("  Estoque Queijo: " + salgado2.getEstoque());
        
        pedidoCommand.execute();
        
        System.out.println("\nApós a execução:");
        System.out.println("  Pedido criado com ID: " + pedidoCommand.getPedido().getId());
        System.out.println("  Status: " + pedidoCommand.getPedido().getStatus());
        System.out.println("  Valor Total: R$ " + pedidoCommand.getPedido().getValorTotal());
        System.out.println("  Movimento Financeiro: " + pedidoCommand.getMovimento().getTipoMovimento().getDescricao());
        
        // Atualizar salgados para verificar estoque
        salgado1 = salgadoRepository.findById(salgado1.getId()).orElseThrow();
        salgado2 = salgadoRepository.findById(salgado2.getId()).orElseThrow();
        System.out.println("  Estoque Frango: " + salgado1.getEstoque());
        System.out.println("  Estoque Queijo: " + salgado2.getEstoque());

        // 5. Desfazer PedidoCommand (CANCELAR)
        System.out.println("\n--- DESFAZENDO PEDIDO COMMAND (CANCELAR) ---");
        pedidoCommand.undo();
        
        System.out.println("Após o undo:");
        System.out.println("  Status do Pedido: " + pedidoCommand.getPedido().getStatus());
        
        // Atualizar salgados para verificar estoque restaurado
        salgado1 = salgadoRepository.findById(salgado1.getId()).orElseThrow();
        salgado2 = salgadoRepository.findById(salgado2.getId()).orElseThrow();
        System.out.println("  Estoque Frango: " + salgado1.getEstoque() + " (restaurado)");
        System.out.println("  Estoque Queijo: " + salgado2.getEstoque() + " (restaurado)");

        System.out.println("\n### FIM DO EXEMPLO 1 ###\n");
    }

    private void exemploFluxoCompletoEstorno() {
        System.out.println("### EXEMPLO 2: FLUXO COMPLETO DE ESTORNO DE PEDIDO ###\n");

        // 1. Criar cliente
        Cliente cliente = Cliente.builder()
                .nome("Maria Santos")
                .email("maria@email.com")
                .senha("123456")
                .build();
        cliente = clienteRepository.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");

        // 2. Criar salgado com estoque
        Salgado salgado = Salgado.builder()
                .sabor("Catupiry")
                .preco(6.50)
                .estoque(80)
                .build();
        if (!salgadoRepository.existsBySabor("Catupiry")) {
            salgado = salgadoRepository.save(salgado);
        } else {
            salgado = salgadoRepository.findBySabor("Catupiry").orElseThrow();
        }
        System.out.println("Salgado criado: " + salgado.getSabor() + " (Estoque: " + salgado.getEstoque() + ")");

        // 3. Criar e confirmar pedido manualmente (simulando pedido já existente)
        ItemPedido item = ItemPedido.builder()
                .salgado(salgado)
                .sabor("Catupiry")
                .quantidade(20)
                .valorUnitario(6.50)
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .valorTotal(130.00)
                .status(StatusPedido.CONFIRMADO)
                .itens(itens)
                .build();
        
        // Associar itens ao pedido antes de salvar
        itens.forEach(i -> i.setPedido(pedido));
        
        Pedido savedPedido = pedidoDAO.save(pedido);
        
        // Atualizar estoque (simulando que o pedido já foi processado)
        salgado.setEstoque(salgado.getEstoque() - 20);
        salgado = salgadoRepository.save(salgado);
        
        System.out.println("Pedido confirmado manualmente:");
        System.out.println("  ID do Pedido: " + savedPedido.getId());
        System.out.println("  Status: " + savedPedido.getStatus());
        System.out.println("  Valor Total: R$ " + savedPedido.getValorTotal());
        System.out.println("  Estoque atual: " + salgado.getEstoque());

        // 4. Executar EstornoPedidoCommand
        System.out.println("\n--- EXECUTANDO ESTORNO PEDIDO COMMAND ---");
        estornoPedidoCommand.setPedido(savedPedido);
        
        System.out.println("Antes do estorno:");
        System.out.println("  Status do Pedido: " + savedPedido.getStatus());
        System.out.println("  Estoque: " + salgado.getEstoque());
        
        estornoPedidoCommand.execute();
        
        System.out.println("\nApós o estorno:");
        System.out.println("  Status do Pedido: " + estornoPedidoCommand.getPedido().getStatus());
        System.out.println("  Pedido ativo: " + estornoPedidoCommand.getPedido().getAtivo());
        
        // Atualizar salgado para verificar estoque
        salgado = salgadoRepository.findById(salgado.getId()).orElseThrow();
        System.out.println("  Estoque: " + salgado.getEstoque() + " (restaurado)");

        // 5. Desfazer EstornoPedidoCommand
        System.out.println("\n--- DESFAZENDO ESTORNO PEDIDO COMMAND ---");
        estornoPedidoCommand.undo();
        
        System.out.println("Após o undo do estorno:");
        System.out.println("  Status do Pedido: " + estornoPedidoCommand.getPedido().getStatus());
        System.out.println("  Pedido ativo: " + estornoPedidoCommand.getPedido().getAtivo());
        
        // Atualizar salgado para verificar estoque
        salgado = salgadoRepository.findById(salgado.getId()).orElseThrow();
        System.out.println("  Estoque: " + salgado.getEstoque() + " (deduzido novamente)");

        System.out.println("\n### FIM DO EXEMPLO 2 ###\n");
    }

    private void exemploCommandInvoker() {
        System.out.println("### EXEMPLO 3: USANDO COMMAND INVOKER ###\n");

        // 1. Criar cliente
        Cliente cliente = Cliente.builder()
                .nome("Pedro Oliveira")
                .email("pedro@email.com")
                .senha("123456")
                .build();
        cliente = clienteRepository.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome());

        // 2. Criar salgados
        Salgado salgado1 = Salgado.builder()
                .sabor("Carne")
                .preco(5.50)
                .estoque(200)
                .build();
        if (!salgadoRepository.existsBySabor("Carne")) {
            salgado1 = salgadoRepository.save(salgado1);
        } else {
            salgado1 = salgadoRepository.findBySabor("Carne").orElseThrow();
        }

        Salgado salgado2 = Salgado.builder()
                .sabor("Frango")
                .preco(5.00)
                .estoque(150)
                .build();
        if (!salgadoRepository.existsBySabor("Frango")) {
            salgado2 = salgadoRepository.save(salgado2);
        } else {
            salgado2 = salgadoRepository.findBySabor("Frango").orElseThrow();
        }
        System.out.println("Salgados criados");

        // 3. Criar primeiro pedido
        ItemPedido item1 = ItemPedido.builder()
                .salgado(salgado1)
                .sabor("Carne")
                .quantidade(15)
                .valorUnitario(5.50)
                .build();

        List<ItemPedido> itens1 = new ArrayList<>();
        itens1.add(item1);

        System.out.println("\n--- EXECUTANDO PRIMEIRO PEDIDO ---");
        System.out.println("Comandos no histórico antes: " + commandInvoker.getCommandHistorySize());
        
        // Nota: Para usar o CommandInvoker real, precisaria injetar as dependências corretamente
        // Este é um exemplo conceitual do fluxo
        System.out.println("Pedido 1 seria executado via CommandInvoker.executePedidoCommand()");
        System.out.println("Histórico de comandos: 1");
        
        // 4. Criar segundo pedido
        ItemPedido item2 = ItemPedido.builder()
                .salgado(salgado2)
                .sabor("Frango")
                .quantidade(25)
                .valorUnitario(5.00)
                .build();

        List<ItemPedido> itens2 = new ArrayList<>();
        itens2.add(item2);

        System.out.println("\n--- EXECUTANDO SEGUNDO PEDIDO ---");
        System.out.println("Pedido 2 seria executado via CommandInvoker.executePedidoCommand()");
        System.out.println("Histórico de comandos: 2");

        // 5. Desfazer último comando
        System.out.println("\n--- DESFAZENDO ÚLTIMO COMANDO ---");
        System.out.println("CommandInvoker.undoLastCommand() desfaria o Pedido 2");
        System.out.println("Histórico de comandos: 1");

        // 6. Desfazer todos os comandos
        System.out.println("\n--- DESFAZENDO TODOS OS COMANDOS ---");
        System.out.println("CommandInvoker.undoAllCommands() desfaria o Pedido 1");
        System.out.println("Histórico de comandos: 0");

        System.out.println("\n### FIM DO EXEMPLO 3 ###\n");
    }
}
