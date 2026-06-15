package com.example.sistemadesalgado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoResponse {
    private Long clienteId;
    private String clienteNome;
    private List<PedidoResponse> pedidos;
    private List<MovimentoResponse> movimentos;
    private Map<String, Double> resumoFinanceiro;
    private Map<String, Long> estatisticasPedidos;
}
