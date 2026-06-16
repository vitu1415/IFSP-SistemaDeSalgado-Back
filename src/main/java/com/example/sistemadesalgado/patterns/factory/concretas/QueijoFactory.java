package com.example.sistemadesalgado.patterns.factory.concretas;

import com.example.sistemadesalgado.patterns.factory.Salgado;
import com.example.sistemadesalgado.patterns.factory.SalgadoQueijo;

public class QueijoFactory extends SalgadoFactory {

    @Override
    public Salgado criarSalgado() {
        return new SalgadoQueijo();
    }
}
