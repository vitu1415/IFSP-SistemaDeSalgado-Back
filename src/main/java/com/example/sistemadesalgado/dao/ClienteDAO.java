package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteDAO {

    Cliente save(Cliente cliente);

    Optional<Cliente> findById(Long id);

    Optional<Cliente> findByEmail(String email);

    List<Cliente> findAll();

    boolean existsByEmail(String email);

    Cliente update(Cliente cliente);

    void deleteById(Long id);
}
