package com.example.sistemadesalgado.controller;

import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.dto.ItemPedidoRequest;
import com.example.sistemadesalgado.model.dto.ItemPedidoResponse;
import com.example.sistemadesalgado.model.dto.PedidoRequest;
import com.example.sistemadesalgado.model.dto.PedidoResponse;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.Salgado;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import com.example.sistemadesalgado.patterns.strategy.CalculoPrecoStrategyFactory;
import com.example.sistemadesalgado.service.ClienteService;
import com.example.sistemadesalgado.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final SalgadoDAO salgadoDAO;
    private final CalculoPrecoStrategyFactory calculoPrecoStrategyFactory;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        var clienteOpt = clienteService.buscarPorId(pedidoRequest.getClienteId());
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PedidoResponse(null, null, null, null, null, null, null, "Cliente não encontrado"));
        }

        Cliente cliente = clienteOpt.get();

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequest itemRequest : pedidoRequest.getItens()) {
            Salgado salgado;
            if (itemRequest.getSalgadoId() != null) {
                salgado = salgadoDAO.findById(itemRequest.getSalgadoId())
                        .orElseThrow(() -> new RuntimeException("Salgado não encontrado"));
            } else {
                salgado = salgadoDAO.findBySabor(itemRequest.getSabor())
                        .orElseThrow(() -> new RuntimeException("Salgado não encontrado"));
            }

            if (salgado.getEstoque() < itemRequest.getQuantidade()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new PedidoResponse(null, null, null, null, null, null, null,
                                "Estoque insuficiente para salgado: " + salgado.getSabor()));
            }

            ItemPedido item = new ItemPedido();
            item.setSalgado(salgado);
            item.setSabor(salgado.getSabor());
            item.setQuantidade(itemRequest.getQuantidade());
            item.setValorUnitario(salgado.getPreco());
            itens.add(item);
        }

        TipoPreco tipoPreco = pedidoRequest.getTipoPreco() == null ? TipoPreco.PADRAO : pedidoRequest.getTipoPreco();
        var strategy = calculoPrecoStrategyFactory.obterStrategy(tipoPreco);

        try {
            Pedido pedido = pedidoService.criarPedido(cliente, itens, strategy, tipoPreco);

            List<ItemPedidoResponse> itensResponse = pedido.getItens().stream()
                    .map(item -> new ItemPedidoResponse(
                            item.getId(),
                            item.getSalgado().getId(),
                            item.getSabor(),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getValorUnitario() * item.getQuantidade()
                    ))
                    .toList();

            PedidoResponse response = new PedidoResponse(
                    pedido.getId(),
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome(),
                    pedido.getValorTotal(),
                    pedido.getStatus(),
                    pedido.getDataCriacao(),
                    pedido.getTipoPreco(),
                    itensResponse,
                    "Pedido criado com sucesso"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PedidoResponse(null, null, null, null, null, null, null,
                            "Erro ao criar pedido: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<PedidoResponse> estornarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.estornarPedido(id);

            List<ItemPedidoResponse> itensResponse = pedido.getItens().stream()
                    .map(item -> new ItemPedidoResponse(
                            item.getId(),
                            item.getSalgado().getId(),
                            item.getSabor(),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getValorUnitario() * item.getQuantidade()
                    ))
                    .toList();

            PedidoResponse response = new PedidoResponse(
                    pedido.getId(),
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome(),
                    pedido.getValorTotal(),
                    pedido.getStatus(),
                    pedido.getDataCriacao(),
                    pedido.getTipoPreco(),
                    itensResponse,
                    "Pedido estornado com sucesso"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PedidoResponse(null, null, null, null, null, null, null,
                            "Erro ao estornar pedido: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.buscarPorId(id);

            List<ItemPedidoResponse> itensResponse = pedido.getItens().stream()
                    .map(item -> new ItemPedidoResponse(
                            item.getId(),
                            item.getSalgado().getId(),
                            item.getSabor(),
                            item.getQuantidade(),
                            item.getValorUnitario(),
                            item.getValorUnitario() * item.getQuantidade()
                    ))
                    .toList();

            PedidoResponse response = new PedidoResponse(
                    pedido.getId(),
                    pedido.getCliente().getId(),
                    pedido.getCliente().getNome(),
                    pedido.getValorTotal(),
                    pedido.getStatus(),
                    pedido.getDataCriacao(),
                    pedido.getTipoPreco(),
                    itensResponse,
                    null
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PedidoResponse(null, null, null, null, null, null, null,
                            "Pedido não encontrado"));
        }
    }
}
