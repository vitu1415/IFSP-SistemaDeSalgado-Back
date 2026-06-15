package com.example.sistemadesalgado.model.dto;

import com.example.sistemadesalgado.model.enums.TipoPreco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    private Long clienteId;
    private List<ItemPedidoRequest> itens;
    private TipoPreco tipoPreco;
}
