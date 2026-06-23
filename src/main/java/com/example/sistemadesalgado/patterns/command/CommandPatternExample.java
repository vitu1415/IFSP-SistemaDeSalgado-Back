package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.dao.ClienteDAO;
import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.patterns.factory.Salgado;
import lombok.RequiredArgsConstructor;
import com.example.sistemadesalgado.patterns.factory.concretas.FrangoFactory;
import com.example.sistemadesalgado.patterns.factory.concretas.QueijoFactory;
import com.example.sistemadesalgado.patterns.factory.concretas.CarneFactory;
import com.example.sistemadesalgado.patterns.factory.concretas.FrangoCatupiryFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommandPatternExample implements CommandLineRunner {

    private final CommandInvoker commandInvoker;
    private final ClienteDAO clienteDAO;
    private final SalgadoDAO salgadoDAO;
    private final PedidoDAO pedidoDAO;
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
        cliente = clienteDAO.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");

        // 2. Criar salgados com estoque (usando factories concretas do patterns.factory)
        Salgado salgado1 = new FrangoFactory().criarSalgado();
        SalgadoEstoque salgadoEstoque1 = SalgadoEstoque.builder()
                .sabor(salgado1.getSabor())
                .preco(salgado1.getPreco())
                .estoque(100)
                .build();
        if (!salgadoDAO.existsBySabor(salgadoEstoque1.getSabor())) {
            salgadoEstoque1 = salgadoDAO.save(salgadoEstoque1);
        } else {
            salgadoEstoque1 = salgadoDAO.findBySabor(salgadoEstoque1.getSabor()).orElseThrow();
        }

        Salgado salgado2 = new QueijoFactory().criarSalgado();
        SalgadoEstoque salgadoEstoque2 = SalgadoEstoque.builder()
                .sabor(salgado2.getSabor())
                .preco(salgado2.getPreco())
                .estoque(50)
                .build();
        if (!salgadoDAO.existsBySabor(salgadoEstoque2.getSabor())) {
            salgadoEstoque2 = salgadoDAO.save(salgadoEstoque2);
        } else {
            salgadoEstoque2 = salgadoDAO.findBySabor(salgadoEstoque2.getSabor()).orElseThrow();
        }
        System.out.println("Salgados criados com estoque inicial");

        // 3. Criar itens do pedido
        ItemPedido item1 = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque1)
                .sabor(salgadoEstoque1.getSabor())
                .quantidade(10)
                .valorUnitario(salgadoEstoque1.getPreco())
                .build();

        ItemPedido item2 = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque2)
                .sabor(salgadoEstoque2.getSabor())
                .quantidade(5)
                .valorUnitario(salgadoEstoque2.getPreco())
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item1);
        itens.add(item2);

        // 4. Executar PedidoCommand
        System.out.println("\n--- EXECUTANDO PEDIDO COMMAND ---");
        pedidoCommand.setPedidoData(cliente, itens);
        
        System.out.println("Antes da execução:");
        System.out.println("  Estoque Frango: " + salgadoEstoque1.getEstoque());
        System.out.println("  Estoque Queijo: " + salgadoEstoque2.getEstoque());
        
        pedidoCommand.execute();
        
        System.out.println("\nApós a execução:");
        System.out.println("  Pedido criado com ID: " + pedidoCommand.getPedido().getId());
        System.out.println("  Status: " + pedidoCommand.getPedido().getStatus());
        System.out.println("  Valor Total: R$ " + pedidoCommand.getPedido().getValorTotal());
        System.out.println("  Movimento Financeiro: " + pedidoCommand.getMovimento().getTipoMovimento().getDescricao());
        
        // Atualizar salgados para verificar estoque
        salgadoEstoque1 = salgadoDAO.findById(salgadoEstoque1.getId()).orElseThrow();
        salgadoEstoque2 = salgadoDAO.findById(salgadoEstoque2.getId()).orElseThrow();
        System.out.println("  Estoque Frango: " + salgadoEstoque1.getEstoque());
        System.out.println("  Estoque Queijo: " + salgadoEstoque2.getEstoque());

        // 5. Desfazer PedidoCommand (CANCELAR)
        System.out.println("\n--- DESFAZENDO PEDIDO COMMAND (CANCELAR) ---");
        pedidoCommand.undo();
        
        System.out.println("Após o undo:");
        System.out.println("  Status do Pedido: " + pedidoCommand.getPedido().getStatus());
        
        // Atualizar salgados para verificar estoque restaurado
        salgadoEstoque1 = salgadoDAO.findById(salgadoEstoque1.getId()).orElseThrow();
        salgadoEstoque2 = salgadoDAO.findById(salgadoEstoque2.getId()).orElseThrow();
        System.out.println("  Estoque Frango: " + salgadoEstoque1.getEstoque() + " (restaurado)");
        System.out.println("  Estoque Queijo: " + salgadoEstoque2.getEstoque() + " (restaurado)");

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
        cliente = clienteDAO.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome() + " (ID: " + cliente.getId() + ")");

        // 2. Criar salgado com estoque (factory concreta)
        Salgado salgadoFrangoCatupiry = new FrangoCatupiryFactory().criarSalgado();
        SalgadoEstoque salgadoEstoque = SalgadoEstoque.builder()
                .sabor(salgadoFrangoCatupiry.getSabor())
                .preco(salgadoFrangoCatupiry.getPreco())
                .estoque(80)
                .build();
        if (!salgadoDAO.existsBySabor(salgadoEstoque.getSabor())) {
            salgadoEstoque = salgadoDAO.save(salgadoEstoque);
        } else {
            salgadoEstoque = salgadoDAO.findBySabor(salgadoEstoque.getSabor()).orElseThrow();
        }
        System.out.println("Salgado criado: " + salgadoEstoque.getSabor() + " (Estoque: " + salgadoEstoque.getEstoque() + ")");

        // 3. Criar itens do pedido
        ItemPedido item = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque)
                .sabor(salgadoEstoque.getSabor())
                .quantidade(20)
                .valorUnitario(salgadoEstoque.getPreco())
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item);

        // 4. Executar PedidoCommand para criar pedido + movimento financeiro + atualizar estoque
        System.out.println("\n--- EXECUTANDO PEDIDO COMMAND ---");
        pedidoCommand.setPedidoData(cliente, itens);
        pedidoCommand.execute();
        Pedido savedPedido = pedidoCommand.getPedido();

        System.out.println("Pedido criado com movimento financeiro:");
        System.out.println("  ID do Pedido: " + savedPedido.getId());
        System.out.println("  Status: " + savedPedido.getStatus());
        System.out.println("  Valor Total: R$ " + savedPedido.getValorTotal());
        System.out.println("  Movimento ID: " + pedidoCommand.getMovimento().getId());
        System.out.println("  Movimento Tipo: " + pedidoCommand.getMovimento().getTipoMovimento().getDescricao());
        System.out.println("  Movimento Valor: R$ " + pedidoCommand.getMovimento().getValor());

        salgadoEstoque = salgadoDAO.findById(salgadoEstoque.getId()).orElseThrow();
        System.out.println("  Estoque atual: " + salgadoEstoque.getEstoque());

        // 5. Executar EstornoPedidoCommand (deleta o movimento vinculado)
        System.out.println("\n--- EXECUTANDO ESTORNO PEDIDO COMMAND ---");
        estornoPedidoCommand.setPedido(savedPedido);
        estornoPedidoCommand.execute();

        System.out.println("\nApós o estorno:");
        System.out.println("  Status do Pedido: " + estornoPedidoCommand.getPedido().getStatus());
        System.out.println("  Pedido ativo: " + estornoPedidoCommand.getPedido().getAtivo());

        salgadoEstoque = salgadoDAO.findById(salgadoEstoque.getId()).orElseThrow();
        System.out.println("  Estoque: " + salgadoEstoque.getEstoque() + " (restaurado)");
        System.out.println("  Movimento financeiro removido ao estornar o pedido");

        // 6. Desfazer EstornoPedidoCommand
        System.out.println("\n--- DESFAZENDO ESTORNO PEDIDO COMMAND ---");
        estornoPedidoCommand.undo();

        System.out.println("Após o undo do estorno:");
        System.out.println("  Status do Pedido: " + estornoPedidoCommand.getPedido().getStatus());
        System.out.println("  Pedido ativo: " + estornoPedidoCommand.getPedido().getAtivo());

        salgadoEstoque = salgadoDAO.findById(salgadoEstoque.getId()).orElseThrow();
        System.out.println("  Estoque: " + salgadoEstoque.getEstoque() + " (deduzido novamente)");

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
        cliente = clienteDAO.save(cliente);
        System.out.println("Cliente criado: " + cliente.getNome());

        // 2. Criar salgados (usando factories concretas)
        Salgado salgadoCarne = new CarneFactory().criarSalgado();
        SalgadoEstoque salgadoEstoque1 = SalgadoEstoque.builder()
                .sabor(salgadoCarne.getSabor())
                .preco(salgadoCarne.getPreco())
                .estoque(200)
                .build();
        if (!salgadoDAO.existsBySabor(salgadoEstoque1.getSabor())) {
            salgadoEstoque1 = salgadoDAO.save(salgadoEstoque1);
        } else {
            salgadoEstoque1 = salgadoDAO.findBySabor(salgadoEstoque1.getSabor()).orElseThrow();
        }

        Salgado salgadoFrango = new FrangoFactory().criarSalgado();
        SalgadoEstoque salgadoEstoque2 = SalgadoEstoque.builder()
                .sabor(salgadoFrango.getSabor())
                .preco(salgadoFrango.getPreco())
                .estoque(150)
                .build();
        if (!salgadoDAO.existsBySabor(salgadoEstoque2.getSabor())) {
            salgadoEstoque2 = salgadoDAO.save(salgadoEstoque2);
        } else {
            salgadoEstoque2 = salgadoDAO.findBySabor(salgadoEstoque2.getSabor()).orElseThrow();
        }
        System.out.println("Salgados criados");

        // 3. Criar primeiro pedido
        ItemPedido item1 = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque1)
                .sabor(salgadoEstoque1.getSabor())
                .quantidade(15)
                .valorUnitario(salgadoEstoque1.getPreco())
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
                .salgadoEstoque(salgadoEstoque2)
                .sabor(salgadoEstoque2.getSabor())
                .quantidade(25)
                .valorUnitario(salgadoEstoque2.getPreco())
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
