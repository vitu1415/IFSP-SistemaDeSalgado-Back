package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import com.example.sistemadesalgado.service.MovimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PedidoCommand implements Command {

    private final PedidoDAO pedidoDAO;
    private final MovimentoService movimentoService;
    private final MovimentoDAO movimentoDAO;
    private final SalgadoDAO salgadoDAO;

    private Cliente cliente;
    private List<ItemPedido> itens;
    private TipoPreco tipoPreco;
    private Pedido pedido;
    private Movimento movimento;
    private List<SalgadoEstoque> salgadosAtualizados;

    public void setPedidoData(Cliente cliente, List<ItemPedido> itens, TipoPreco tipoPreco) {
        this.cliente = cliente;
        this.itens = itens;
        this.tipoPreco = tipoPreco == null ? TipoPreco.PADRAO : tipoPreco;
        this.salgadosAtualizados = new ArrayList<>();
    }

    public void setPedidoData(Cliente cliente, List<ItemPedido> itens) {
        setPedidoData(cliente, itens, TipoPreco.PADRAO);
    }

    @Override
    public void execute() {
        // 1. Criar pedido
        pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setAtivo(true);
        pedido.setTipoPreco(tipoPreco);
        pedido.setValorTotal(calcularValorTotal());
        pedido.setItens(itens);

        // 2. Associar itens ao pedido antes de salvar
        itens.forEach(item -> item.setPedido(pedido));

        // 3. Salvar pedido
        Pedido savedPedido = pedidoDAO.save(pedido);
        pedido = savedPedido;

        // 4. Gerar movimento financeiro (concluido) vinculado ao pedido
        movimento = movimentoService.criarMovimento(cliente, TipoMovimento.Concluido, pedido.getValorTotal(), pedido);

        // 5. Atualizar estoque dos salgados
        for (ItemPedido item : itens) {
            SalgadoEstoque salgadoEstoque = salgadoDAO.findById(item.getSalgadoEstoque().getId()).orElseThrow();
            Integer estoqueAtual = salgadoEstoque.getEstoque();
            Integer novoEstoque = estoqueAtual - item.getQuantidade();
            
            if (novoEstoque < 0) {
                throw new RuntimeException("Estoque insuficiente para salgado: " + salgadoEstoque.getSabor());
            }
            
            salgadoEstoque.setEstoque(novoEstoque);
            salgadoEstoque = salgadoDAO.update(salgadoEstoque);
            salgadosAtualizados.add(salgadoEstoque);
        }

        // 6. Atualizar status para CONFIRMADO
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido = pedidoDAO.update(pedido);

        System.out.println("PedidoCommand.execute() - Pedido criado e confirmado com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Valor Total: R$ " + pedido.getValorTotal());
        System.out.println("Movimento Financeiro: R$ " + movimento.getValor() + " (" + movimento.getTipoMovimento().getDescricao() + ")");
    }

    @Override
    public void undo() {
        if (pedido == null) {
            throw new RuntimeException("Não há pedido para desfazer");
        }

        // 1. Restaurar estoque dos salgados
        for (ItemPedido item : itens) {
            SalgadoEstoque salgadoEstoque = salgadoDAO.findById(item.getSalgadoEstoque().getId()).orElseThrow();
            Integer estoqueAtual = salgadoEstoque.getEstoque();
            Integer novoEstoque = estoqueAtual + item.getQuantidade();
            salgadoEstoque.setEstoque(novoEstoque);
            salgadoDAO.update(salgadoEstoque);
        }

        // 2. Remover movimento financeiro
        if (movimento != null) {
            movimentoDAO.deleteById(movimento.getId());
        }

        // 3. Atualizar status para ESTORNADO
        pedido.setStatus(StatusPedido.ESTORNADO);
        pedido.setAtivo(false);
        pedidoDAO.update(pedido);

        System.out.println("PedidoCommand.undo() - Pedido desfeito com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Estoque restaurado");
        System.out.println("Movimento financeiro removido");
    }

    private Double calcularValorTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getValorUnitario() * item.getQuantidade())
                .sum();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Movimento getMovimento() {
        return movimento;
    }
}
