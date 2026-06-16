package com.example.sistemadesalgado.patterns.factory.concretas;

import com.example.sistemadesalgado.patterns.factory.Salgado;
import com.example.sistemadesalgado.patterns.factory.SalgadoFrangoCatupiry;

public class FrangoCatupiryFactory extends SalgadoFactory {

    @Override
    public Salgado criarSalgado() {
        return new SalgadoFrangoCatupiry();
    }
}