package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;

@Component
@RequiredArgsConstructor
public class CommandInvoker {

    private final PedidoCommand pedidoCommand;
    private final EstornoPedidoCommand estornoPedidoCommand;

    private final Stack<Command> commandHistory = new Stack<>();

    public void executePedidoCommand(Cliente cliente, List<ItemPedido> itens, TipoPreco tipoPreco) {
        pedidoCommand.setPedidoData(cliente, itens, tipoPreco);
        pedidoCommand.execute();
        commandHistory.push(pedidoCommand);
    }

    public void executeEstornoCommand(Pedido pedido) {
        estornoPedidoCommand.setPedido(pedido);
        estornoPedidoCommand.execute();
        commandHistory.push(estornoPedidoCommand);
    }

    public int getCommandHistorySize() {
        return commandHistory.size();
    }

}
