package com.example.sistemadesalgado.patterns.factory.concretas;

import com.example.sistemadesalgado.patterns.factory.Salgado;
import com.example.sistemadesalgado.patterns.factory.SalgadoFrango;

public class FrangoFactory extends SalgadoFactory {

    @Override
    public Salgado criarSalgado() {
        return new SalgadoFrango();
    }
}
