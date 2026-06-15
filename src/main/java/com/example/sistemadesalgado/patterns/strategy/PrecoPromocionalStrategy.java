package com.example.sistemadesalgado.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class PrecoPromocionalStrategy implements CalculoPrecoStrategy {

    private static final double DESCONTO_PROMOCIONAL = 0.10;

    @Override
    public Double calcularPreco(Double precoBase, Integer quantidade) {
        return precoBase * quantidade * (1 - DESCONTO_PROMOCIONAL);
    }

    @Override
    public String getDescricao() {
        return "Preco promocional com 10% de desconto";
    }
}
