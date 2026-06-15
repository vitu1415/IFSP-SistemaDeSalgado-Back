package com.example.sistemadesalgado.patterns.strategy;

class CalculoPrecoStrategyExample {

    public static void main(String[] args) {
        // Exemplo 1: Usando estratégia padrão
        CalculoPrecoContext contexto1 = new CalculoPrecoContext();
        Double precoBase = 5.00;
        Integer quantidade = 10;
        
        Double precoPadrao = contexto1.executarCalculo(precoBase, quantidade);
        System.out.println("Exemplo 1 - Preço Padrão:");
        System.out.println("Estratégia: " + contexto1.getDescricaoStrategy());
        System.out.println("Preço base: R$ " + precoBase);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Preço final: R$ " + precoPadrao);
        System.out.println();

        // Exemplo 2: Criando novo contexto com estratégia padrão
        CalculoPrecoContext contexto2 = new CalculoPrecoContext();
        Double precoBase2 = 6.50;
        Integer quantidade2 = 5;
        
        Double precoPadrao2 = contexto2.executarCalculo(precoBase2, quantidade2);
        System.out.println("Exemplo 2 - Novo pedido com Preço Padrão:");
        System.out.println("Estratégia: " + contexto2.getDescricaoStrategy());
        System.out.println("Preço base: R$ " + precoBase2);
        System.out.println("Quantidade: " + quantidade2);
        System.out.println("Preço final: R$ " + precoPadrao2);
        System.out.println();

        // Exemplo 3: Comparação de diferentes pedidos
        System.out.println("Exemplo 3 - Comparação de pedidos:");
        Double precoComparacao = 10.00;
        Integer qtdComparacao = 20;
        
        CalculoPrecoContext ctxPadrao = new CalculoPrecoContext();
        
        System.out.println("Preço base: R$ " + precoComparacao);
        System.out.println("Quantidade: " + qtdComparacao);
        System.out.println();
        System.out.println("Preço Padrão: R$ " + ctxPadrao.executarCalculo(precoComparacao, qtdComparacao));
    }
}
