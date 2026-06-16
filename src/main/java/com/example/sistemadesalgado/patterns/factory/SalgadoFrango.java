package com.example.sistemadesalgado.patterns.factory;

public class SalgadoFrango implements Salgado {

    @Override
    public String getSabor() {
        return "Frango";
    }

    @Override
    public double getPreco() {
        return 8.00;
    }

    @Override
    public void preparar() {
        System.out.println("Preparando salgado de frango");
    }
}