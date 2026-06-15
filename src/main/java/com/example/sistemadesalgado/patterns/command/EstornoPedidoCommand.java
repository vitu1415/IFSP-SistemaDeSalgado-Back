package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.Salgado;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EstornoPedidoCommand implements Command {

    private final PedidoDAO pedidoDAO;
    private final MovimentoDAO movimentoDAO;
    private final SalgadoDAO salgadoDAO;

    private Pedido pedido;
    private Movimento movimentoEstorno;
    private List<ItemPedido> itens;

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        this.itens = pedido.getItens();
    }

    @Override
    public void execute() {
        if (pedido == null) {
            throw new RuntimeException("Pedido não informado para estorno");
        }

        if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
            throw new RuntimeException("Apenas pedidos confirmados podem ser estornados. Status atual: " + pedido.getStatus());
        }

        // 1. Restaurar estoque dos salgados
        for (ItemPedido item : itens) {
            Salgado salgado = salgadoDAO.findById(item.getSalgado().getId()).orElseThrow();
            Integer estoqueAtual = salgado.getEstoque();
            Integer novoEstoque = estoqueAtual + item.getQuantidade();
            salgado.setEstoque(novoEstoque);
            salgadoDAO.update(salgado);
        }

        // 2. Gerar movimento de estorno (crédito)
        movimentoEstorno = new Movimento();
        movimentoEstorno.setTipoMovimento(TipoMovimento.CREDITO);
        movimentoEstorno.setValor(pedido.getValorTotal());
        movimentoEstorno.setDataHora(LocalDateTime.now());
        movimentoEstorno.setCliente(pedido.getCliente());
        movimentoEstorno = movimentoDAO.save(movimentoEstorno);

        // 3. Atualizar status para ESTORNADO
        pedido.setStatus(StatusPedido.ESTORNADO);
        pedidoDAO.update(pedido);

        System.out.println("EstornoPedidoCommand.execute() - Estorno realizado com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Valor Estornado: R$ " + movimentoEstorno.getValor());
        System.out.println("Movimento de Estorno: R$ " + movimentoEstorno.getValor() + " (" + movimentoEstorno.getTipoMovimento().getDescricao() + ")");
        System.out.println("Estoque restaurado para todos os itens");
    }

    @Override
    public void undo() {
        if (pedido == null) {
            throw new RuntimeException("Não há estorno para desfazer");
        }

        // 1. Remover movimento de estorno
        if (movimentoEstorno != null) {
            movimentoDAO.deleteById(movimentoEstorno.getId());
        }

        // 2. Deduzir estoque dos salgados (desfazer a restauração)
        for (ItemPedido item : itens) {
            Salgado salgado = salgadoDAO.findById(item.getSalgado().getId()).orElseThrow();
            Integer estoqueAtual = salgado.getEstoque();
            Integer novoEstoque = estoqueAtual - item.getQuantidade();
            
            if (novoEstoque < 0) {
                throw new RuntimeException("Estoque insuficiente para desfazer estorno do salgado: " + salgado.getSabor());
            }
            
            salgado.setEstoque(novoEstoque);
            salgadoDAO.update(salgado);
        }

        // 3. Atualizar status de volta para CONFIRMADO
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedidoDAO.update(pedido);

        System.out.println("EstornoPedidoCommand.undo() - Estorno desfeito com sucesso");
        System.out.println("ID do Pedido: " + pedido.getId());
        System.out.println("Movimento de estorno removido");
        System.out.println("Estoque deduzido novamente");
        System.out.println("Status do pedido: " + pedido.getStatus());
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Movimento getMovimentoEstorno() {
        return movimentoEstorno;
    }
}
