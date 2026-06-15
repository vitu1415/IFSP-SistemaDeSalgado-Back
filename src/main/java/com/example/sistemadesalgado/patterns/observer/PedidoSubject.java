package com.example.sistemadesalgado.patterns.observer;

import com.example.sistemadesalgado.model.entity.Pedido;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class PedidoSubject {

    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer adicionado: " + observer.getClass().getSimpleName());
        }
    }

    public void detach(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removido: " + observer.getClass().getSimpleName());
    }

    public void notifyObservers(Pedido pedido) {
        System.out.println("\n=== NOTIFICANDO OBSERVERS ===");
        System.out.println("Pedido ID: " + pedido.getId());
        System.out.println("Status: " + pedido.getStatus());
        System.out.println("Valor Total: R$ " + pedido.getValorTotal());
        System.out.println("Número de Observers: " + observers.size());
        System.out.println();

        for (Observer observer : observers) {
            System.out.println("Notificando: " + observer.getClass().getSimpleName());
            observer.update(pedido);
            System.out.println();
        }

        System.out.println("=== FIM DA NOTIFICAÇÃO ===\n");
    }

    public int getObserverCount() {
        return observers.size();
    }
}
