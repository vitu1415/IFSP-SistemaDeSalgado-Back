package com.example.sistemadesalgado.patterns.strategy;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CalculoPrecoContext {

    private CalculoPrecoStrategy strategy;

    public CalculoPrecoContext() {
        this.strategy = new PrecoPadraoStrategy();
    }

    public void setStrategy(CalculoPrecoStrategy strategy) {
        this.strategy = strategy;
    }

    public Double executarCalculo(Double precoBase, Integer quantidade) {
        return strategy.calcularPreco(precoBase, quantidade);
    }

    public String getDescricaoStrategy() {
        return strategy.getDescricao();
    }
}
