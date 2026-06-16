package com.example.sistemadesalgado.repository;

import com.example.sistemadesalgado.model.entity.SalgadoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalgadoRepository extends JpaRepository<SalgadoEstoque, Long> {

    Optional<SalgadoEstoque> findBySabor(String sabor);

    List<SalgadoEstoque> findByEstoqueGreaterThan(Integer estoque);

    List<SalgadoEstoque> findByEstoqueGreaterThanOrderByPrecoAsc(Integer estoque);

    List<SalgadoEstoque> findBySaborContainingIgnoreCase(String sabor);

    boolean existsBySabor(String sabor);
}
