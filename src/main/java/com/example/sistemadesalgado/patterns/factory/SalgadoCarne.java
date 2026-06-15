package com.example.sistemadesalgado.patterns.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalgadoCarne extends Salgado {

    public SalgadoCarne(Integer estoque) {
        this.sabor = "Carne";
        this.preco = 5.50;
        this.estoque = estoque;
    }

    @Override
    public String getDescricao() {
        return "Salgado de carne moída temperada";
    }

    @Override
    public Double calcularPreco(Integer quantidade) {
        return this.preco * quantidade;
    }
}
