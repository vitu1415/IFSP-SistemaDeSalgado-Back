package com.example.sistemadesalgado.patterns.strategy;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CalculoPrecoStrategyTest {

    @Test
    void deveCalcularPrecoPadraoSemDesconto() {
        CalculoPrecoStrategy strategy = new PrecoPadraoStrategy();

        Double total = strategy.calcularPreco(5.00, 10);

        assertThat(total).isEqualTo(50.00);
    }

    @Test
    void deveCalcularPrecoPromocionalComDezPorCentoDeDesconto() {
        CalculoPrecoStrategy strategy = new PrecoPromocionalStrategy();

        Double total = strategy.calcularPreco(5.00, 10);

        assertThat(total).isEqualTo(45.00);
    }
}
