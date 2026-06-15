package com.example.sistemadesalgado.controller;

import com.example.sistemadesalgado.exception.ResourceNotFoundException;
import com.example.sistemadesalgado.mapper.PedidoMapper;
import com.example.sistemadesalgado.model.dto.PedidoRequest;
import com.example.sistemadesalgado.model.dto.PedidoResponse;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
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
    private final PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(@RequestBody PedidoRequest pedidoRequest) {
        Cliente cliente = clienteService.buscarPorId(pedidoRequest.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        List<ItemPedido> itens = new ArrayList<>();
        Pedido pedido = pedidoService.criarPedido(cliente, itens, pedidoRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pedidoMapper.toResponse(pedido, "Pedido criado com sucesso"));
    }

    @PostMapping("/{id}/estornar")
    public ResponseEntity<PedidoResponse> estornarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.estornarPedido(id);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido, "Pedido estornado com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedidoMapper.toResponse(pedido));
    }
}
