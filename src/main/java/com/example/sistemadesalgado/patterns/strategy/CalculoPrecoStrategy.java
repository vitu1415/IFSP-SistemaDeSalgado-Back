package com.example.sistemadesalgado.patterns.strategy;

public interface CalculoPrecoStrategy {

    Double calcularPreco(Double precoBase, Integer quantidade);

    String getDescricao();
}
