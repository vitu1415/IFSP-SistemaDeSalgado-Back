package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.SalgadoEstoque;

import java.util.List;
import java.util.Optional;

public interface SalgadoDAO {

    SalgadoEstoque save(SalgadoEstoque salgadoEstoque);

    Optional<SalgadoEstoque> findById(Long id);

    Optional<SalgadoEstoque> findBySabor(String sabor);

    boolean existsBySabor(String sabor);

    List<SalgadoEstoque> findAll();

    SalgadoEstoque update(SalgadoEstoque salgadoEstoque);

    void deleteById(Long id);
}
