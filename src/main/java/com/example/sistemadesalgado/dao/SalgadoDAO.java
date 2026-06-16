package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.SalgadoEstoque;

import java.util.List;
import java.util.Optional;

public interface SalgadoDAO {

    SalgadoEstoque save(SalgadoEstoque salgadoEstoque);

    Optional<SalgadoEstoque> findById(Long id);

    Optional<SalgadoEstoque> findBySabor(String sabor);

    List<SalgadoEstoque> findAll();

    List<SalgadoEstoque> findByEstoqueGreaterThan(Integer estoque);

    List<SalgadoEstoque> findByEstoqueGreaterThanOrderByPrecoAsc(Integer estoque);

    List<SalgadoEstoque> findBySaborContainingIgnoreCase(String sabor);

    boolean existsBySabor(String sabor);

    SalgadoEstoque update(SalgadoEstoque salgadoEstoque);

    void deleteById(Long id);
}
