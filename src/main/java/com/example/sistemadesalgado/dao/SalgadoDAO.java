package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.Salgado;

import java.util.List;
import java.util.Optional;

public interface SalgadoDAO {

    Salgado save(Salgado salgado);

    Optional<Salgado> findById(Long id);

    Optional<Salgado> findBySabor(String sabor);

    List<Salgado> findAll();

    List<Salgado> findByEstoqueGreaterThan(Integer estoque);

    List<Salgado> findByEstoqueGreaterThanOrderByPrecoAsc(Integer estoque);

    List<Salgado> findBySaborContainingIgnoreCase(String sabor);

    boolean existsBySabor(String sabor);

    Salgado update(Salgado salgado);

    void deleteById(Long id);
}
