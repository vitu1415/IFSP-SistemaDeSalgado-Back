package com.example.sistemadesalgado.patterns.observer;

import com.example.sistemadesalgado.dao.SalgadoDAO;
import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.entity.Salgado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EstoqueObserver implements Observer {

    private final SalgadoDAO salgadoDAO;

    @Override
    public void update(Pedido pedido) {
        System.out.println(">>> EstoqueObserver.update() - Iniciando atualização de estoque");
        System.out.println("    Pedido ID: " + pedido.getId());
        System.out.println("    Status: " + pedido.getStatus());

        Map<String, Integer> estoqueAntes = new HashMap<>();
        Map<String, Integer> estoqueDepois = new HashMap<>();

        // Registrar estoque antes da atualização
        for (ItemPedido item : pedido.getItens()) {
            Salgado salgado = salgadoDAO.findById(item.getSalgado().getId()).orElseThrow();
            estoqueAntes.put(salgado.getSabor(), salgado.getEstoque());
        }

        // Atualizar estoque baseado no status do pedido
        if (pedido.getStatus().toString().equals("CONFIRMADO")) {
            // Deduzir estoque quando pedido é confirmado
            for (ItemPedido item : pedido.getItens()) {
                Salgado salgado = salgadoDAO.findById(item.getSalgado().getId()).orElseThrow();
                Integer estoqueAtual = salgado.getEstoque();
                Integer novoEstoque = estoqueAtual - item.getQuantidade();
                
                if (novoEstoque < 0) {
                    throw new RuntimeException("Estoque insuficiente para salgado: " + salgado.getSabor());
                }
                
                salgado.setEstoque(novoEstoque);
                salgadoDAO.update(salgado);
                estoqueDepois.put(salgado.getSabor(), novoEstoque);
                
                System.out.println("    Salgado: " + salgado.getSabor() + 
                                 " | Quantidade: " + item.getQuantidade() +
                                 " | Estoque antes: " + estoqueAtual + 
                                 " | Estoque depois: " + novoEstoque);
            }
        } else if (pedido.getStatus().toString().equals("ESTORNADO")) {
            // Restaurar estoque quando pedido é estornado
            for (ItemPedido item : pedido.getItens()) {
                Salgado salgado = salgadoDAO.findById(item.getSalgado().getId()).orElseThrow();
                Integer estoqueAtual = salgado.getEstoque();
                Integer novoEstoque = estoqueAtual + item.getQuantidade();
                
                salgado.setEstoque(novoEstoque);
                salgadoDAO.update(salgado);
                estoqueDepois.put(salgado.getSabor(), novoEstoque);
                
                System.out.println("    Salgado: " + salgado.getSabor() + 
                                 " | Quantidade restaurada: " + item.getQuantidade() +
                                 " | Estoque antes: " + estoqueAtual + 
                                 " | Estoque depois: " + novoEstoque);
            }
        } else {
            System.out.println("    Status " + pedido.getStatus() + " não requer atualização de estoque");
        }

        System.out.println(">>> EstoqueObserver.update() - Atualização de estoque concluída");
    }
}
