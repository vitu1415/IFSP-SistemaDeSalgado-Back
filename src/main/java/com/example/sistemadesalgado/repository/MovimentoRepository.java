package com.example.sistemadesalgado.repository;

import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentoRepository extends JpaRepository<Movimento, Long> {

    List<Movimento> findByClienteId(Long clienteId);

    List<Movimento> findByClienteIdOrderByDataHoraDesc(Long clienteId);

    List<Movimento> findByTipoMovimento(TipoMovimento tipoMovimento);

    List<Movimento> findByClienteIdAndTipoMovimento(Long clienteId, TipoMovimento tipoMovimento);

    List<Movimento> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Movimento m WHERE m.cliente.id = :clienteId AND m.dataHora >= :startDate ORDER BY m.dataHora DESC")
    List<Movimento> findHistoricoMovimentosByClienteAndData(@Param("clienteId") Long clienteId, @Param("startDate") LocalDateTime startDate);

    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimento m WHERE m.cliente.id = :clienteId AND m.tipoMovimento = :tipoMovimento")
    Double sumValorByClienteAndTipoMovimento(@Param("clienteId") Long clienteId, @Param("tipoMovimento") TipoMovimento tipoMovimento);
}
