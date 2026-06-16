package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EstornoPedidoCommand implements Command {

    private final PedidoDAO pedidoDAO;
    private final SalgadoDAO salgadoDAO;

    private Pedido pedido;
    private List<ItemPedido> itens;

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        this.itens = pedido.getItens();
    }

    @Override
    public void execute() {
        if (pedido == null) {
            throw new RuntimeException("Pedido nao informado para estorno");
        }

        if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new RuntimeException("Apenas pedidos confirmados podem ser estornados. Status atual: " + pedido.getStatus());
        }

        for (ItemPedido item : itens) {
            SalgadoEstoque salgadoEstoque = salgadoDAO.findById(item.getSalgadoEstoque().getId()).orElseThrow();
            Integer estoqueAtual = salgadoEstoque.getEstoque();
            Integer novoEstoque = estoqueAtual + item.getQuantidade();
            salgadoEstoque.setEstoque(novoEstoque);
            salgadoDAO.update(salgadoEstoque);
        }

        pedido.setStatus(StatusPedido.ESTORNADO);
        pedido.setAtivo(false);
        pedidoDAO.update(pedido);

        System.out.println("EstornoPedidoCommand.execute() - Estorno realizado com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Pedido marcado como inativo");
        System.out.println("Estoque restaurado para todos os itens");
    }

    @Override
    public void undo() {
        if (pedido == null) {
            throw new RuntimeException("Nao ha estorno para desfazer");
        }

        for (ItemPedido item : itens) {
            SalgadoEstoque salgadoEstoque = salgadoDAO.findById(item.getSalgadoEstoque().getId()).orElseThrow();
            Integer estoqueAtual = salgadoEstoque.getEstoque();
            Integer novoEstoque = estoqueAtual - item.getQuantidade();

            if (novoEstoque < 0) {
                throw new RuntimeException("Estoque insuficiente para desfazer estorno do salgado: " + salgadoEstoque.getSabor());
            }

            salgadoEstoque.setEstoque(novoEstoque);
            salgadoDAO.update(salgadoEstoque);
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setAtivo(true);
        pedidoDAO.update(pedido);

        System.out.println("EstornoPedidoCommand.undo() - Estorno desfeito com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Estoque deduzido novamente");
        System.out.println("Status do pedido: " + pedido.getStatus());
        System.out.println("Pedido ativo: " + pedido.getAtivo());
    }

    public Pedido getPedido() {
        return pedido;
    }
}
