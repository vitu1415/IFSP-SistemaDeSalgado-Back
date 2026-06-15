package com.example.sistemadesalgado.patterns.strategy;

import com.example.sistemadesalgado.model.enums.TipoPreco;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalculoPrecoStrategyFactory {

    private final PrecoPadraoStrategy precoPadraoStrategy;
    private final PrecoPromocionalStrategy precoPromocionalStrategy;

    public CalculoPrecoStrategy obterStrategy(TipoPreco tipoPreco) {
        if (tipoPreco == TipoPreco.PROMOCIONAL) {
            return precoPromocionalStrategy;
        }

        return precoPadraoStrategy;
    }
}
