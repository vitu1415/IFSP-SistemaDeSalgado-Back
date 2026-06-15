package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.Salgado;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import com.example.sistemadesalgado.patterns.command.CommandInvoker;
import com.example.sistemadesalgado.patterns.factory.SalgadoFactory;
import com.example.sistemadesalgado.patterns.observer.PedidoSubject;
import com.example.sistemadesalgado.patterns.state.PedidoContext;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoContext;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final SalgadoDAO salgadoDAO;
    private final MovimentoService movimentoService;
    private final CommandInvoker commandInvoker;
    private final PedidoSubject pedidoSubject;
    private final CalculoPrecoContext calculoPrecoContext;

    public Pedido criarPedido(Cliente cliente, List<ItemPedido> itens, CalculoPrecoStrategy strategy, TipoPreco tipoPreco) {
        // Usa Factory Pattern para criar salgados se necessário
        for (ItemPedido item : itens) {
            if (item.getSalgado() == null) {
                Salgado salgadoEntity = salgadoDAO.findBySabor(item.getSabor())
                        .orElseThrow(() -> new RuntimeException("Salgado não encontrado: " + item.getSabor()));
                item.setSalgado(salgadoEntity);
            }
            
            // Calcula preço usando Strategy
            calculoPrecoContext.setStrategy(strategy);
            Double precoCalculado = calculoPrecoContext.executarCalculo(
                item.getValorUnitario(), 
                item.getQuantidade()
            );
            // Atualiza valor unitário se necessário
            item.setValorUnitario(precoCalculado / item.getQuantidade());
        }

        // Usa Command Pattern para criar pedido
        commandInvoker.executePedidoCommand(cliente, itens, tipoPreco);
        
        // Obtém o pedido criado pelo comando
        Pedido pedido = pedidoDAO.findByClienteId(cliente.getId()).stream()
                .reduce((first, second) -> second)
                .orElseThrow(() -> new RuntimeException("Falha ao criar pedido"));

        // Usa Observer Pattern para notificar observers
        pedidoSubject.notifyObservers(pedido);

        // Usa State Pattern para gerenciar estado
        PedidoContext pedidoContext = new PedidoContext();
        pedidoContext.preparar(); // CRIADO -> PREPARANDO

        return pedido;
    }

    public Pedido buscarPorId(Long id) {
        return pedidoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + id));
    }

    public List<Pedido> buscarPorCliente(Long clienteId) {
        return pedidoDAO.findByClienteId(clienteId);
    }

    public List<Pedido> buscarPorClienteOrdenado(Long clienteId) {
        return pedidoDAO.findByClienteIdOrderByDataCriacaoDesc(clienteId);
    }

    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoDAO.findByStatus(status);
    }

    public List<Pedido> buscarPorClienteEStatus(Long clienteId, StatusPedido status) {
        return pedidoDAO.findByClienteIdAndStatus(clienteId, status);
    }

    public Pedido estornarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        
        // Usa Command Pattern para estornar pedido
        commandInvoker.executeEstornoCommand(pedido);
        
        // Usa Observer Pattern para notificar observers
        pedidoSubject.notifyObservers(pedido);
        
        // Usa State Pattern para gerenciar estado
        PedidoContext pedidoContext = new PedidoContext();
        pedidoContext.cancelar(); // CANCELADO (simula estorno como cancelamento)

        return pedido;
    }

    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido.setStatus(novoStatus);
        pedido = pedidoDAO.update(pedido);
        
        // Usa Observer Pattern para notificar mudança de status
        pedidoSubject.notifyObservers(pedido);
        
        return pedido;
    }

    public List<Pedido> buscarHistoricoPorClienteEData(Long clienteId, java.time.LocalDateTime startDate) {
        return pedidoDAO.findHistoricoPedidosByClienteAndData(clienteId, startDate);
    }

    public List<Pedido> buscarPorClienteEStatuses(Long clienteId, List<StatusPedido> statuses) {
        return pedidoDAO.findPedidosByClienteAndStatuses(clienteId, statuses);
    }

    public void deletarPedido(Long id) {
        if (!pedidoDAO.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + id);
        }
        pedidoDAO.deleteById(id);
    }

    public Double calcularValorTotalPedido(List<ItemPedido> itens, CalculoPrecoStrategy strategy) {
        final var currentStrategy = strategy;
        
        return itens.stream()
                .mapToDouble(item -> {
                    calculoPrecoContext.setStrategy(currentStrategy);
                    return calculoPrecoContext.executarCalculo(
                        item.getValorUnitario(), 
                        item.getQuantidade()
                    );
                })
                .sum();
    }
}
