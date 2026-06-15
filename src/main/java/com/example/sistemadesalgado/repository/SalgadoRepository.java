package com.example.sistemadesalgado.repository;

import com.example.sistemadesalgado.model.entity.Salgado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalgadoRepository extends JpaRepository<Salgado, Long> {

    Optional<Salgado> findBySabor(String sabor);

    List<Salgado> findByEstoqueGreaterThan(Integer estoque);

    List<Salgado> findByEstoqueGreaterThanOrderByPrecoAsc(Integer estoque);

    List<Salgado> findBySaborContainingIgnoreCase(String sabor);

    boolean existsBySabor(String sabor);
}
