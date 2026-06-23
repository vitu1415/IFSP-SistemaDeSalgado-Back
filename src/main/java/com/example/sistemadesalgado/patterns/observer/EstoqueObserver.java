package com.example.sistemadesalgado.patterns.observer;

import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EstoqueObserver implements Observer {

    private static final int LIMITE_ESTOQUE_BAIXO = 10;

    private final SalgadoDAO salgadoDAO;

    @Override
    public void update(Pedido pedido) {
        System.out.println(">>> EstoqueObserver - Verificando níveis de estoque");

        for (ItemPedido item : pedido.getItens()) {
            SalgadoEstoque salgadoEstoque = salgadoDAO.findById(item.getSalgadoEstoque().getId()).orElseThrow();
            Integer estoqueAtual = salgadoEstoque.getEstoque();

            System.out.println("    " + salgadoEstoque.getSabor() +
                             " | Estoque atual: " + estoqueAtual);

            if (estoqueAtual < LIMITE_ESTOQUE_BAIXO) {
                System.out.println("    >>> ALERTA: Estoque baixo para " + salgadoEstoque.getSabor() +
                                 " | Estoque: " + estoqueAtual);
            }
        }

        System.out.println(">>> EstoqueObserver - Verificação concluída");
    }
}
