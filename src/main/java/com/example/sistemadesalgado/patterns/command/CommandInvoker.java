package com.example.sistemadesalgado.patterns.command;

import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
@RequiredArgsConstructor
public class CommandInvoker {

    private final PedidoCommand pedidoCommand;
    private final EstornoPedidoCommand estornoPedidoCommand;

    private final Stack<Command> commandHistory = new Stack<>();

    public void executePedidoCommand(Cliente cliente, java.util.List<com.example.sistemadesalgado.model.entity.ItemPedido> itens, TipoPreco tipoPreco) {
        pedidoCommand.setPedidoData(cliente, itens, tipoPreco);
        pedidoCommand.execute();
        commandHistory.push(pedidoCommand);
    }

    public void executeEstornoCommand(com.example.sistemadesalgado.model.entity.Pedido pedido) {
        estornoPedidoCommand.setPedido(pedido);
        estornoPedidoCommand.execute();
        commandHistory.push(estornoPedidoCommand);
    }

    public void undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        } else {
            throw new RuntimeException("Não há comandos para desfazer");
        }
    }

    public void undoAllCommands() {
        while (!commandHistory.isEmpty()) {
            Command lastCommand = commandHistory.pop();
            lastCommand.undo();
        }
    }

    public int getCommandHistorySize() {
        return commandHistory.size();
    }

    public boolean hasCommandsToUndo() {
        return !commandHistory.isEmpty();
    }
}
