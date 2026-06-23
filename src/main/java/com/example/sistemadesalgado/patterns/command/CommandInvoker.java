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

    public Pedido executePedidoCommand(Cliente cliente, List<ItemPedido> itens, TipoPreco tipoPreco) {
        pedidoCommand.setPedidoData(cliente, itens, tipoPreco);
        pedidoCommand.execute();
        commandHistory.push(pedidoCommand);
        return pedidoCommand.getPedido();
    }

    public Pedido executeEstornoCommand(Pedido pedido) {
        estornoPedidoCommand.setPedido(pedido);
        estornoPedidoCommand.execute();
        commandHistory.push(estornoPedidoCommand);
        return estornoPedidoCommand.getPedido();
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            commandHistory.pop().undo();
        }
    }

    public int getCommandHistorySize() {
        return commandHistory.size();
    }

}
