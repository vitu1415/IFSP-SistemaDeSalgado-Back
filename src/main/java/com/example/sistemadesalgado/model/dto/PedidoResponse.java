package com.example.sistemadesalgado.model.dto;

import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoPreco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Double valorTotal;
    private StatusPedido status;
    private LocalDateTime dataCriacao;
    private Boolean ativo;
    private TipoPreco tipoPreco;
    private List<ItemPedidoResponse> itens;
    private String mensagem;

    public PedidoResponse(Long id, Long clienteId, String clienteNome, Double valorTotal,
                          StatusPedido status, LocalDateTime dataCriacao,
                          List<ItemPedidoResponse> itens, String mensagem) {
        this(id, clienteId, clienteNome, valorTotal, status, dataCriacao, null, null, itens, mensagem);
    }

    public PedidoResponse(Long id, Long clienteId, String clienteNome, Double valorTotal,
                          StatusPedido status, LocalDateTime dataCriacao,
                          TipoPreco tipoPreco, List<ItemPedidoResponse> itens, String mensagem) {
        this(id, clienteId, clienteNome, valorTotal, status, dataCriacao, null, tipoPreco, itens, mensagem);
    }
}
