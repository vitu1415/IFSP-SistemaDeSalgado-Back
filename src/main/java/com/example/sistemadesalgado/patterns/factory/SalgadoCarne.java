package com.example.sistemadesalgado.patterns.factory;

public class SalgadoCarne implements Salgado {

    @Override
    public String getSabor() {
        return "Carne";
    }

    @Override
    public double getPreco() {
        return 8.50;
    }

    @Override
    public void preparar() {
        System.out.println("Preparando salgado de carne");
    }
}
