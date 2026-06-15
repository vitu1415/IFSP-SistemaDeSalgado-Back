package com.example.sistemadesalgado.patterns.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SalgadoQueijo extends Salgado {

    public SalgadoQueijo(Integer estoque) {
        this.sabor = "Queijo";
        this.preco = 4.50;
        this.estoque = estoque;
    }

    @Override
    public String getDescricao() {
        return "Salgado de queijo mussarela derretido";
    }

    @Override
    public Double calcularPreco(Integer quantidade) {
        return this.preco * quantidade;
    }
}
