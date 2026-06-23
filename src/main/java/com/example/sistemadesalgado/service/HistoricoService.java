package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.ClienteDAO;
import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.exception.ResourceNotFoundException;
import com.example.sistemadesalgado.builder.PedidoMapper;
import com.example.sistemadesalgado.model.dto.HistoricoResponse;
import com.example.sistemadesalgado.model.entity.Movimento;
import com.example.sistemadesalgado.model.entity.Pedido;
import com.example.sistemadesalgado.model.enums.StatusPedido;
import com.example.sistemadesalgado.model.enums.TipoMovimento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoricoService {

    private static final int DIAS_HISTORICO = 30;

    private final PedidoDAO pedidoDAO;
    private final MovimentoDAO movimentoDAO;

    public List<Pedido> buscarHistoricoPedidos(Long clienteId, LocalDateTime startDate) {
        return pedidoDAO.findHistoricoPedidosByClienteAndData(clienteId, startDate);
    }

    public List<Movimento> buscarHistoricoMovimentos(Long clienteId, LocalDateTime startDate) {
        return movimentoDAO.findHistoricoMovimentosByClienteAndData(clienteId, startDate);
    }

    public Double calcularTotalGasto(Long clienteId, LocalDateTime startDate) {
        List<Pedido> pedidos = buscarHistoricoPedidos(clienteId, startDate);
        return pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.CONFIRMADO)
                .mapToDouble(Pedido::getValorTotal)
                .sum();
    }

    public Double calcularTotalEstornado(Long clienteId, LocalDateTime startDate) {
        List<Pedido> pedidos = buscarHistoricoPedidos(clienteId, startDate);
        return pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.ESTORNADO)
                .mapToDouble(Pedido::getValorTotal)
                .sum();
    }

    public Map<String, Double> gerarResumoFinanceiro(Long clienteId, LocalDateTime startDate) {
        Map<String, Double> resumo = new HashMap<>();
        
        Double totalCreditos = movimentoDAO.sumValorByClienteAndTipoMovimento(
            clienteId, TipoMovimento.Estorno
        );
        Double totalDebitos = movimentoDAO.sumValorByClienteAndTipoMovimento(
            clienteId, TipoMovimento.Concluido
        );
        
        if (totalCreditos == null) totalCreditos = 0.0;
        if (totalDebitos == null) totalDebitos = 0.0;
        
        resumo.put("totalCreditos", totalCreditos);
        resumo.put("totalDebitos", totalDebitos);
        resumo.put("saldo", totalCreditos - totalDebitos);
        resumo.put("totalGasto", calcularTotalGasto(clienteId, startDate));
        resumo.put("totalEstornado", calcularTotalEstornado(clienteId, startDate));
        
        return resumo;
    }

    public Map<String, Long> gerarEstatisticasPedidos(Long clienteId, LocalDateTime startDate) {
        Map<String, Long> estatisticas = new HashMap<>();
        
        List<Pedido> pedidos = buscarHistoricoPedidos(clienteId, startDate);
        
        estatisticas.put("total", (long) pedidos.size());
        estatisticas.put("confirmados", pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.CONFIRMADO).count());
        estatisticas.put("estornados", pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.ESTORNADO).count());
        estatisticas.put("criados", pedidos.stream()
                .filter(p -> p.getStatus() == StatusPedido.CRIADO).count());
        
        return estatisticas;
    }
}
