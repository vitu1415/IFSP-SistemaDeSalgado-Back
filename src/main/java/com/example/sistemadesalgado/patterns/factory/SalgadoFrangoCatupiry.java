package com.example.sistemadesalgado.patterns.factory;

public class SalgadoFrangoCatupiry implements Salgado {

    @Override
    public String getSabor() {
        return "Frango com Catupiry";
    }

    @Override
    public double getPreco() {
        return 9.50;
    }

    @Override
    public void preparar() {
        System.out.println("Preparando salgado de frango com catupiry");
    }
}
