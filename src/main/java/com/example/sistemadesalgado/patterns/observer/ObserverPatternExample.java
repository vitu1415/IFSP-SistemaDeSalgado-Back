package com.example.sistemadesalgado.patterns.observer;

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
public class ObserverPatternExample implements CommandLineRunner {

    private final PedidoSubject pedidoSubject;
    private final EstoqueObserver estoqueObserver;
    private final HistoricoObserver historicoObserver;
    private final ClienteDAO clienteDAO;
    private final SalgadoDAO salgadoDAO;
    private final PedidoDAO pedidoDAO;

    @Override
    public void run(String... args) {
        System.out.println("=== INICIANDO EXEMPLOS DO OBSERVER PATTERN ===\n");

        // Exemplo 1: Criação de pedido com notificação de observers
        exemploCriacaoPedido();

        // Exemplo 2: Estorno de pedido com notificação
        exemploEstornoPedido();

        // Exemplo 3: Gerenciamento dinâmico de observers
        exemploGerenciamentoObservers();

        System.out.println("=== EXEMPLOS DO OBSERVER PATTERN CONCLUÍDOS ===");
    }

    private void exemploCriacaoPedido() {
        System.out.println("### EXEMPLO 1: CRIAÇÃO DE PEDIDO ###\n");

        // 1. Criar ou buscar cliente
        Cliente cliente;
        if (clienteDAO.existsByEmail("carlos@email.com")) {
            cliente = clienteDAO.findByEmail("carlos@email.com").orElseThrow();
        } else {
            cliente = Cliente.builder()
                    .nome("Carlos Eduardo")
                    .email("carlos@email.com")
                    .senha("123456")
                    .build();
            cliente = clienteDAO.save(cliente);
        }
        System.out.println("Cliente: " + cliente.getNome());

        // 2. Criar ou buscar salgados
        SalgadoEstoque salgadoEstoque1;
        if (salgadoDAO.existsBySabor("Frango")) {
            salgadoEstoque1 = salgadoDAO.findBySabor("Frango").orElseThrow();
        } else {
            Salgado salgado1 = new FrangoFactory().criarSalgado();
            salgadoEstoque1 = SalgadoEstoque.builder()
                    .sabor(salgado1.getSabor())
                    .preco(salgado1.getPreco())
                    .estoque(100)
                    .build();
            salgadoEstoque1 = salgadoDAO.save(salgadoEstoque1);
        }

        SalgadoEstoque salgadoEstoque2;
        if (salgadoDAO.existsBySabor("Queijo")) {
            salgadoEstoque2 = salgadoDAO.findBySabor("Queijo").orElseThrow();
        } else {
            Salgado salgado2 = new QueijoFactory().criarSalgado();
            salgadoEstoque2 = SalgadoEstoque.builder()
                    .sabor(salgado2.getSabor())
                    .preco(salgado2.getPreco())
                    .estoque(80)
                    .build();
            salgadoEstoque2 = salgadoDAO.save(salgadoEstoque2);
        }

        System.out.println("Salgados:");
        System.out.println("  - Frango: " + salgadoEstoque1.getEstoque() + " unidades");
        System.out.println("  - Queijo: " + salgadoEstoque2.getEstoque() + " unidades");

        // 3. Criar itens
        ItemPedido item1 = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque1)
                .sabor("Frango")
                .quantidade(10)
                .valorUnitario(5.00)
                .build();

        ItemPedido item2 = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque2)
                .sabor("Queijo")
                .quantidade(15)
                .valorUnitario(4.50)
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item1);
        itens.add(item2);

        // 4. Criar pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .valorTotal(117.50)
                .status(StatusPedido.CRIADO)
                .itens(itens)
                .build();
        itens.forEach(i -> i.setPedido(pedido));
        Pedido savedPedido = pedidoDAO.save(pedido);

        System.out.println("\n--- ANTES DA NOTIFICAÇÃO ---");
        System.out.println("Status: " + savedPedido.getStatus());
        System.out.println("Observers: " + pedidoSubject.getObserverCount());

        // 5. Confirmar e notificar
        savedPedido.setStatus(StatusPedido.CONFIRMADO);
        savedPedido = pedidoDAO.update(savedPedido);

        System.out.println("\n--- NOTIFICANDO OBSERVERS ---");
        pedidoSubject.notifyObservers(savedPedido);

        // 6. Verificar estoque
        salgadoEstoque1 = salgadoDAO.findById(salgadoEstoque1.getId()).orElseThrow();
        salgadoEstoque2 = salgadoDAO.findById(salgadoEstoque2.getId()).orElseThrow();

        System.out.println("--- APÓS NOTIFICAÇÃO ---");
        System.out.println("Estoque atualizado:");
        System.out.println("  - Frango: " + salgadoEstoque1.getEstoque() + " unidades");
        System.out.println("  - Queijo: " + salgadoEstoque2.getEstoque() + " unidades");

        System.out.println("\n### FIM DO EXEMPLO 1 ###\n");
    }

    private void exemploEstornoPedido() {
        System.out.println("### EXEMPLO 2: ESTORNO DE PEDIDO ###\n");

        // 1. Criar ou buscar cliente
        Cliente cliente;
        if (clienteDAO.existsByEmail("ana@email.com")) {
            cliente = clienteDAO.findByEmail("ana@email.com").orElseThrow();
        } else {
            cliente = Cliente.builder()
                    .nome("Ana Paula")
                    .email("ana@email.com")
                    .senha("123456")
                    .build();
            cliente = clienteDAO.save(cliente);
        }
        System.out.println("Cliente: " + cliente.getNome());

        // 2. Criar ou buscar salgado
        SalgadoEstoque salgadoEstoque;
        if (salgadoDAO.existsBySabor("Catupiry")) {
            salgadoEstoque = salgadoDAO.findBySabor("Catupiry").orElseThrow();
        } else {
            Salgado domain = new FrangoCatupiryFactory().criarSalgado();
            salgadoEstoque = SalgadoEstoque.builder()
                    .sabor(domain.getSabor())
                    .preco(domain.getPreco())
                    .estoque(50)
                    .build();
            salgadoEstoque = salgadoDAO.save(salgadoEstoque);
        }
        System.out.println("Salgado: " + salgadoEstoque.getSabor() + " (Estoque: " + salgadoEstoque.getEstoque() + ")");

        // 3. Criar pedido confirmado
        ItemPedido item = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque)
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
        itens.forEach(i -> i.setPedido(pedido));
        Pedido savedPedido = pedidoDAO.save(pedido);

        // Deduzir estoque
        salgadoEstoque.setEstoque(salgadoEstoque.getEstoque() - 20);
        salgadoEstoque = salgadoDAO.update(salgadoEstoque);

        System.out.println("Pedido confirmado (ID: " + savedPedido.getId() + ")");
        System.out.println("Estoque após dedução: " + salgadoEstoque.getEstoque());

        // 4. Estornar e notificar
        System.out.println("\n--- ESTORNANDO PEDIDO ---");
        savedPedido.setStatus(StatusPedido.ESTORNADO);
        savedPedido = pedidoDAO.update(savedPedido);
        pedidoSubject.notifyObservers(savedPedido);

        // 5. Verificar estoque
        salgadoEstoque = salgadoDAO.findById(salgadoEstoque.getId()).orElseThrow();
        System.out.println("--- APÓS ESTORNO ---");
        System.out.println("Estoque restaurado: " + salgadoEstoque.getEstoque() + " unidades");

        System.out.println("\n### FIM DO EXEMPLO 2 ###\n");
    }

    private void exemploGerenciamentoObservers() {
        System.out.println("### EXEMPLO 3: GERENCIAMENTO DE OBSERVERS ###\n");

        System.out.println("Observers iniciais: " + pedidoSubject.getObserverCount());

        // Adicionar observers
        System.out.println("\n--- ADICIONANDO OBSERVERS ---");
        pedidoSubject.attach(estoqueObserver);
        pedidoSubject.attach(historicoObserver);
        System.out.println("Observers após adição: " + pedidoSubject.getObserverCount());

        // Criar pedido simples
        Cliente cliente;
        if (clienteDAO.existsByEmail("teste@email.com")) {
            cliente = clienteDAO.findByEmail("teste@email.com").orElseThrow();
        } else {
            cliente = Cliente.builder()
                    .nome("Teste Observer")
                    .email("teste@email.com")
                    .senha("123456")
                    .build();
            cliente = clienteDAO.save(cliente);
        }

        SalgadoEstoque salgadoEstoque;
        if (salgadoDAO.existsBySabor("Carne")) {
            salgadoEstoque = salgadoDAO.findBySabor("Carne").orElseThrow();
        } else {
            com.example.sistemadesalgado.patterns.factory.Salgado domain = new CarneFactory().criarSalgado();
            salgadoEstoque = SalgadoEstoque.builder()
                    .sabor(domain.getSabor())
                    .preco(domain.getPreco())
                    .estoque(30)
                    .build();
            salgadoEstoque = salgadoDAO.save(salgadoEstoque);
        }

        ItemPedido item = ItemPedido.builder()
                .salgadoEstoque(salgadoEstoque)
                .sabor("Carne")
                .quantidade(5)
                .valorUnitario(5.50)
                .build();

        List<ItemPedido> itens = new ArrayList<>();
        itens.add(item);

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .valorTotal(27.50)
                .status(StatusPedido.CONFIRMADO)
                .itens(itens)
                .build();
        itens.forEach(i -> i.setPedido(pedido));
        Pedido savedPedido = pedidoDAO.save(pedido);

        System.out.println("\n--- NOTIFICANDO COM TODOS OS OBSERVERS ---");
        pedidoSubject.notifyObservers(savedPedido);

        // Remover observer
        System.out.println("\n--- REMOVENDO HISTORICO OBSERVER ---");
        pedidoSubject.detach(historicoObserver);
        System.out.println("Observers após remoção: " + pedidoSubject.getObserverCount());

        // Re-adicionar
        System.out.println("\n--- READICIONANDO HISTORICO OBSERVER ---");
        pedidoSubject.attach(historicoObserver);
        System.out.println("Observers finais: " + pedidoSubject.getObserverCount());

        System.out.println("\n### FIM DO EXEMPLO 3 ###\n");
    }
}
