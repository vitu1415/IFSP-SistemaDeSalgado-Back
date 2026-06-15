package com.example.sistemadesalgado.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoRequest {
    private Long salgadoId;
    private String sabor;
    private Integer quantidade;
    private Double valorUnitario;
}
