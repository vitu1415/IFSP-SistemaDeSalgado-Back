package com.example.sistemadesalgado.builder;

import com.example.sistemadesalgado.model.dto.ItemPedidoResponse;
import com.example.sistemadesalgado.model.dto.MovimentoResponse;
import com.example.sistemadesalgado.model.dto.PedidoResponse;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapper {

    public ItemPedidoResponse toItemResponse(ItemPedido item) {
        return new ItemPedidoResponse(
                item.getId(),
                item.getSalgadoEstoque().getId(),
                item.getSabor(),
                item.getQuantidade(),
                item.getValorUnitario(),
                item.getValorUnitario() * item.getQuantidade()
        );
    }

    public PedidoResponse toResponse(Pedido pedido) {
        return toResponse(pedido, null);
    }

    public PedidoResponse toResponse(Pedido pedido, String mensagem) {
        List<ItemPedidoResponse> itens = pedido.getItens().stream()
                .map(this::toItemResponse)
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                pedido.getAtivo(),
                pedido.getTipoPreco(),
                itens,
                mensagem
        );
    }
}
