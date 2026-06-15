package com.example.sistemadesalgado.patterns.observer;

import com.example.sistemadesalgado.model.entity.ItemPedido;
import com.example.sistemadesalgado.model.entity.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class HistoricoObserver implements Observer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void update(Pedido pedido) {
        System.out.println(">>> HistoricoObserver.update() - Registrando histórico do pedido");
        System.out.println("    Pedido ID: " + pedido.getId());
        System.out.println("    Cliente: " + pedido.getCliente().getNome());
        System.out.println("    Email: " + pedido.getCliente().getEmail());
        System.out.println("    Status: " + pedido.getStatus());
        System.out.println("    Data de Criação: " + pedido.getDataCriacao().format(FORMATTER));
        System.out.println("    Valor Total: R$ " + pedido.getValorTotal());
        
        System.out.println("    Itens do Pedido:");
        for (ItemPedido item : pedido.getItens()) {
            System.out.println("      - " + item.getSabor() + 
                             " | Quantidade: " + item.getQuantidade() +
                             " | Valor Unitário: R$ " + item.getValorUnitario() +
                             " | Subtotal: R$ " + (item.getValorUnitario() * item.getQuantidade()));
        }

        System.out.println("    Timestamp do Registro: " + LocalDateTime.now().format(FORMATTER));
        System.out.println("    Ação: " + getAcaoDescricao(pedido.getStatus().toString()));
        System.out.println(">>> HistoricoObserver.update() - Histórico registrado com sucesso");
    }

    private String getAcaoDescricao(String status) {
        return switch (status) {
            case "CRIADO" -> "Pedido criado e aguardando confirmação";
            case "CONFIRMADO" -> "Pedido confirmado e processado";
            case "ESTORNADO" -> "Pedido estornado e valor devolvido";
            default -> "Status desconhecido: " + status;
        };
    }
}
