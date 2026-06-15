package com.example.sistemadesalgado.patterns.factory;

public class SalgadoFactory {

    public static Salgado criarSalgado(String sabor, Integer estoque) {
        return switch (sabor.toLowerCase()) {
            case "frango" -> new SalgadoFrango(estoque);
            case "catupiry" -> new SalgadoCatupiry(estoque);
            case "carne" -> new SalgadoCarne(estoque);
            case "queijo" -> new SalgadoQueijo(estoque);
            default -> throw new IllegalArgumentException("Sabor de salgado não suportado: " + sabor);
        };
    }

    public static Salgado criarSalgado(String sabor) {
        return criarSalgado(sabor, 0);
    }
}
