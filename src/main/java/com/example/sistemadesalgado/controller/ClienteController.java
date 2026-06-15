package com.example.sistemadesalgado.controller;

import com.example.sistemadesalgado.model.dto.HistoricoResponse;
import com.example.sistemadesalgado.model.dto.LoginRequest;
import com.example.sistemadesalgado.model.dto.LoginResponse;
import com.example.sistemadesalgado.service.ClienteService;
import com.example.sistemadesalgado.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final HistoricoService historicoService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        var clienteOpt = clienteService.buscarPorEmail(loginRequest.getEmail());
        
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Email não encontrado"));
        }
        
        var cliente = clienteOpt.get();
        
        if (!cliente.getSenha().equals(loginRequest.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, null, null, "Senha incorreta"));
        }
        

        return ResponseEntity.ok(new LoginResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "Login realizado com sucesso"
        ));
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<HistoricoResponse> buscarHistorico(
            @PathVariable Long id) {
        
        var clienteOpt = clienteService.buscarPorId(id);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        var cliente = clienteOpt.get();
        
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        
        var pedidos = historicoService.buscarHistoricoPedidos(id, startDate);
        var movimentos = historicoService.buscarHistoricoMovimentos(id, startDate);
        var resumoFinanceiro = historicoService.gerarResumoFinanceiro(id, startDate);
        var estatisticas = historicoService.gerarEstatisticasPedidos(id, startDate);
        
        HistoricoResponse response = new HistoricoResponse();
        response.setClienteId(cliente.getId());
        response.setClienteNome(cliente.getNome());
        response.setPedidos(pedidos.stream()
                .map(p -> new com.example.sistemadesalgado.model.dto.PedidoResponse(
                        p.getId(),
                        p.getCliente().getId(),
                        p.getCliente().getNome(),
                        p.getValorTotal(),
                        p.getStatus(),
                        p.getDataCriacao(),
                        p.getTipoPreco(),
                        p.getItens().stream()
                                .map(item -> new com.example.sistemadesalgado.model.dto.ItemPedidoResponse(
                                        item.getId(),
                                        item.getSalgado().getId(),
                                        item.getSabor(),
                                        item.getQuantidade(),
                                        item.getValorUnitario(),
                                        item.getValorUnitario() * item.getQuantidade()
                                ))
                                .toList(),
                        null
                ))
                .toList());
        response.setMovimentos(movimentos.stream()
                .map(m -> new com.example.sistemadesalgado.model.dto.MovimentoResponse(
                        m.getId(),
                        m.getTipoMovimento(),
                        m.getValor(),
                        m.getDataHora()
                ))
                .toList());
        response.setResumoFinanceiro(resumoFinanceiro);
        response.setEstatisticasPedidos(estatisticas);
        
        return ResponseEntity.ok(response);
    }
}
