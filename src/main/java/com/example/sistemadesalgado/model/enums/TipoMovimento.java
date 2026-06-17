package com.example.sistemadesalgado.model.enums;

import lombok.Getter;

@Getter
public enum TipoMovimento {
    Estorno("Estorno"),
    Concluido("Concluido");

    private final String descricao;

    TipoMovimento(String descricao) {
        this.descricao = descricao;
    }
}
