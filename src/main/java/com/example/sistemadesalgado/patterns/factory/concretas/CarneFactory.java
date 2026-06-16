package com.example.sistemadesalgado.patterns.factory.concretas;

import com.example.sistemadesalgado.patterns.factory.Salgado;
import com.example.sistemadesalgado.patterns.factory.SalgadoCarne;

public class CarneFactory extends SalgadoFactory {

    @Override
    public Salgado criarSalgado() {
        return new SalgadoCarne();
    }
}
