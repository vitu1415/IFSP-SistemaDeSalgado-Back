package com.example.sistemadesalgado.patterns.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Salgado {

    protected String sabor;
    protected Double preco;
    protected Integer estoque;

    public abstract String getDescricao();

    public abstract Double calcularPreco(Integer quantidade);
}
