package com.example.sistemadesalgado.patterns.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalgadoFrango extends Salgado {

    public SalgadoFrango(Integer estoque) {
        this.sabor = "Frango";
        this.preco = 5.00;
        this.estoque = estoque;
    }

    @Override
    public String getDescricao() {
        return "Salgado de frango com recheio suculento";
    }

    @Override
    public Double calcularPreco(Integer quantidade) {
        return this.preco * quantidade;
    }
}
