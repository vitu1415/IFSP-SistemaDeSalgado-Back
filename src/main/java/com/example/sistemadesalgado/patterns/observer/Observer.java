package com.example.sistemadesalgado.patterns.observer;

import com.example.sistemadesalgado.model.entity.Pedido;

public interface Observer {

    void update(Pedido pedido);
}
