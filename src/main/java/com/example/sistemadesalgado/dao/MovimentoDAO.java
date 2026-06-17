package com.example.sistemadesalgado.dao;

import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.enums.TipoMovimento;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimentoDAO {

    Movimento save(Movimento movimento);

    Optional<Movimento> findById(Long id);

    List<Movimento> findByClienteId(Long clienteId);

    List<Movimento> findByClienteIdOrderByDataHoraDesc(Long clienteId);

    List<Movimento> findByTipoMovimento(TipoMovimento tipoMovimento);

    List<Movimento> findByClienteIdAndTipoMovimento(Long clienteId, TipoMovimento tipoMovimento);

    List<Movimento> findByPedidoId(Long pedidoId);

    List<Movimento> findByDataHoraBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Movimento> findHistoricoMovimentosByClienteAndData(Long clienteId, LocalDateTime startDate);

    Double sumValorByClienteAndTipoMovimento(Long clienteId, TipoMovimento tipoMovimento);

    void deleteById(Long id);

    void delete(Movimento movimento);
}
