package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovimentoService {

    private final MovimentoDAO movimentoDAO;

    public Movimento criarMovimento(Cliente cliente, TipoMovimento tipoMovimento, Double valor) {
        return criarMovimento(cliente, tipoMovimento, valor, null);
    }

    public Movimento criarMovimento(Cliente cliente, TipoMovimento tipoMovimento, Double valor, Pedido pedido) {
        Movimento movimento = new Movimento();
        movimento.setCliente(cliente);
        movimento.setTipoMovimento(tipoMovimento);
        movimento.setValor(valor);
        movimento.setDataHora(LocalDateTime.now());
        movimento.setPedido(pedido);
        return movimentoDAO.save(movimento);
    }

    public Movimento buscarPorId(Long id) {
        Optional<Movimento> movimento = movimentoDAO.findById(id);
        if (movimento.isEmpty()) {
            throw new RuntimeException("Movimento não encontrado com ID: " + id);
        }
        return movimento.get();
    }

    public List<Movimento> buscarPorCliente(Long clienteId) {
        return movimentoDAO.findByClienteId(clienteId);
    }

    public List<Movimento> buscarPorClienteOrdenado(Long clienteId) {
        return movimentoDAO.findByClienteIdOrderByDataHoraDesc(clienteId);
    }

    public List<Movimento> buscarPorTipo(TipoMovimento tipoMovimento) {
        return movimentoDAO.findByTipoMovimento(tipoMovimento);
    }

    public List<Movimento> buscarPorClienteETipo(Long clienteId, TipoMovimento tipoMovimento) {
        return movimentoDAO.findByClienteIdAndTipoMovimento(clienteId, tipoMovimento);
    }

    public List<Movimento> buscarPorPeriodo(LocalDateTime startDate, LocalDateTime endDate) {
        return movimentoDAO.findByDataHoraBetween(startDate, endDate);
    }

    public List<Movimento> buscarHistoricoPorClienteEData(Long clienteId, LocalDateTime startDate) {
        return movimentoDAO.findHistoricoMovimentosByClienteAndData(clienteId, startDate);
    }

    public Double somarValorPorClienteETipo(Long clienteId, TipoMovimento tipoMovimento) {
        return movimentoDAO.sumValorByClienteAndTipoMovimento(clienteId, tipoMovimento);
    }

    public void deletarMovimento(Long id) {
        if (movimentoDAO.findById(id) == null) {
            throw new RuntimeException("Movimento não encontrado com ID: " + id);
        }
        movimentoDAO.deleteById(id);
    }
}
