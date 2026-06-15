package com.example.sistemadesalgado.patterns.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalgadoCatupiry extends Salgado {

    public SalgadoCatupiry(Integer estoque) {
        this.sabor = "Catupiry";
        this.preco = 6.50;
        this.estoque = estoque;
    }

    @Override
    public String getDescricao() {
        return "Salgado com recheio especial de catupiry cremoso";
    }

    @Override
    public Double calcularPreco(Integer quantidade) {
        return this.preco * quantidade;
    }
}
