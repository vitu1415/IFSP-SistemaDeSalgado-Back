package com.example.sistemadesalgado.patterns.strategy;

import org.springframework.stereotype.Component;

@Component
public class PrecoPadraoStrategy implements CalculoPrecoStrategy {

    @Override
    public Double calcularPreco(Double precoBase, Integer quantidade) {
        return precoBase * quantidade;
    }

    @Override
    public String getDescricao() {
        return "Preço padrão sem descontos";
    }
}
