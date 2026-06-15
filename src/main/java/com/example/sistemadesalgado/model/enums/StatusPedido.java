package com.example.sistemadesalgado.model.enums;

import lombok.Getter;

@Getter
public enum StatusPedido {
    CRIADO("Criado"),
    CONFIRMADO("Confirmado"),
    ESTORNADO("Estornado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }
}
