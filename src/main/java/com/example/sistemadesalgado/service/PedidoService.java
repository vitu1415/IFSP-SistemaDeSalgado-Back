package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.exception.BusinessException;
import com.example.sistemadesalgado.exception.ResourceNotFoundException;
import com.example.sistemadesalgado.model.dto.ItemPedidoRequest;
import com.example.sistemadesalgado.model.dto.PedidoRequest;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import com.example.sistemadesalgado.patterns.command.CommandInvoker;
import com.example.sistemadesalgado.patterns.observer.EstoqueObserver;
import com.example.sistemadesalgado.patterns.observer.HistoricoObserver;
import com.example.sistemadesalgado.patterns.observer.PedidoSubject;
import com.example.sistemadesalgado.patterns.state.PedidoContext;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoContext;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoStrategy;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoStrategyFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final SalgadoDAO salgadoDAO;
    private final CommandInvoker commandInvoker;
    private final CalculoPrecoStrategyFactory calculoPrecoStrategyFactory;
    private final PedidoSubject pedidoSubject;
    private final CalculoPrecoContext calculoPrecoContext;
    private final HistoricoObserver historicoObserver;
    private final EstoqueObserver estoqueObserver;

    @PostConstruct
    public void init() {
        pedidoSubject.attach(historicoObserver);
        pedidoSubject.attach(estoqueObserver);
    }

    public Pedido criarPedido(Cliente cliente, List<ItemPedido> itens, PedidoRequest pedidoRequest) {
        for (ItemPedidoRequest itemRequest : pedidoRequest.getItens()) {
            SalgadoEstoque salgadoEstoque;
            if (itemRequest.getSalgadoId() != null) {
                salgadoEstoque = salgadoDAO.findById(itemRequest.getSalgadoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Salgado não encontrado"));
            } else {
                throw new BusinessException("ID do salgado invalido, salgado não encontrado");
            }

            if (salgadoEstoque.getEstoque() < itemRequest.getQuantidade()) {
                throw new BusinessException("Estoque insuficiente para salgado: " + salgadoEstoque.getSabor());
            }

            ItemPedido item = new ItemPedido();
            item.setSalgadoEstoque(salgadoEstoque);
            item.setSabor(salgadoEstoque.getSabor());
            item.setQuantidade(itemRequest.getQuantidade());
            item.setValorUnitario(salgadoEstoque.getPreco());
            itens.add(item);
        }
            
        TipoPreco tipoPreco = pedidoRequest.getTipoPreco() == null ? TipoPreco.PADRAO : pedidoRequest.getTipoPreco();
        CalculoPrecoStrategy strategy = calculoPrecoStrategyFactory.obterStrategy(tipoPreco);

        for(ItemPedido item : itens){
            // Calcula preço usando Strategy
            calculoPrecoContext.setStrategy(strategy);
            Double precoCalculado = calculoPrecoContext.executarCalculo(
                    item.getValorUnitario(),
                    item.getQuantidade()
            );
            // Atualiza valor unitário se necessário
            item.setValorUnitario(precoCalculado / item.getQuantidade());
        }

        Pedido pedido = commandInvoker.executePedidoCommand(cliente, itens, tipoPreco);

        pedidoSubject.notifyObservers(pedido);

        stateTransition(pedido, "preparar");

        return pedido;
    }

    public List<Pedido> listarTodos() {
        return pedidoDAO.findAllOrderByDataCriacaoDesc();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com ID: " + id));
    }

    public Pedido estornarPedido(Long pedidoId) {
        Pedido pedido = buscarPorId(pedidoId);
        pedido = commandInvoker.executeEstornoCommand(pedido);

        pedidoSubject.notifyObservers(pedido);

        stateTransition(pedido, "cancelar");

        return pedido;
    }

    private void stateTransition(Pedido pedido, String acao) {
        PedidoContext pedidoContext = new PedidoContext(pedido);
        boolean sucesso = switch (acao) {
            case "preparar" -> pedidoContext.preparar();
            case "entregar" -> pedidoContext.entregar();
            case "cancelar" -> pedidoContext.cancelar();
            default -> false;
        };
        if (sucesso) {
            pedidoDAO.update(pedido);
        }
    }
}
