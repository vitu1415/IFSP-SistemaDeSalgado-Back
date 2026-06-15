package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.ClienteDAO;
import com.example.sistemadesalgado.dao.MovimentoDAO;
import com.example.sistemadesalgado.dao.PedidoDAO;
import com.example.sistemadesalgado.exception.ResourceNotFoundException;
import com.example.sistemadesalgado.mapper.PedidoMapper;
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
    private final ClienteDAO clienteDAO;
    private final PedidoMapper pedidoMapper;

    public HistoricoResponse buscarHistoricoCliente(Long clienteId) {
        var cliente = clienteDAO.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId));

        LocalDateTime startDate = LocalDateTime.now().minusDays(DIAS_HISTORICO);

        var pedidos = buscarHistoricoPedidos(clienteId, startDate);
        var movimentos = buscarHistoricoMovimentos(clienteId, startDate);

        HistoricoResponse response = new HistoricoResponse();
        response.setClienteId(cliente.getId());
        response.setClienteNome(cliente.getNome());
        response.setPedidos(pedidos.stream().map(pedidoMapper::toResponse).toList());
        response.setMovimentos(movimentos.stream().map(pedidoMapper::toMovimentoResponse).toList());
        response.setResumoFinanceiro(gerarResumoFinanceiro(clienteId, startDate));
        response.setEstatisticasPedidos(gerarEstatisticasPedidos(clienteId, startDate));

        return response;
    }

    public List<Pedido> buscarHistoricoPedidos(Long clienteId, LocalDateTime startDate) {
        return pedidoDAO.findHistoricoPedidosByClienteAndData(clienteId, startDate);
    }

    public List<Movimento> buscarHistoricoMovimentos(Long clienteId, LocalDateTime startDate) {
        return movimentoDAO.findHistoricoMovimentosByClienteAndData(clienteId, startDate);
    }

    public Map<String, Object> buscarHistoricoCompleto(Long clienteId, LocalDateTime startDate) {
        Map<String, Object> historico = new HashMap<>();
        
        List<Pedido> pedidos = buscarHistoricoPedidos(clienteId, startDate);
        List<Movimento> movimentos = buscarHistoricoMovimentos(clienteId, startDate);
        
        historico.put("pedidos", pedidos);
        historico.put("movimentos", movimentos);
        historico.put("totalPedidos", pedidos.size());
        historico.put("totalMovimentos", movimentos.size());
        
        return historico;
    }

    public List<Pedido> buscarPedidosPorStatus(Long clienteId, List<StatusPedido> statuses) {
        return pedidoDAO.findPedidosByClienteAndStatuses(clienteId, statuses);
    }

    public List<Pedido> buscarPedidosConfirmados(Long clienteId) {
        return pedidoDAO.findByClienteIdAndStatus(clienteId, StatusPedido.CONFIRMADO);
    }

    public List<Pedido> buscarPedidosEstornados(Long clienteId) {
        return pedidoDAO.findByClienteIdAndStatus(clienteId, StatusPedido.ESTORNADO);
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
            clienteId, TipoMovimento.CREDITO
        );
        Double totalDebitos = movimentoDAO.sumValorByClienteAndTipoMovimento(
            clienteId, TipoMovimento.DEBITO
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
