package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.ClienteDAO;
import com.example.sistemadesalgado.model.entity.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public Cliente criarCliente(Cliente cliente) {
        if (clienteDAO.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + cliente.getEmail());
        }
        return clienteDAO.save(cliente);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteDAO.findById(id);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteDAO.findByEmail(email);
    }

    public List<Cliente> listarTodos() {
        return clienteDAO.findAll();
    }

    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + id));

        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
            clienteDAO.existsByEmail(clienteAtualizado.getEmail())) {
            throw new RuntimeException("Email já cadastrado: " + clienteAtualizado.getEmail());
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setSenha(clienteAtualizado.getSenha());

        return clienteDAO.update(clienteExistente);
    }

    public void deletarCliente(Long id) {
        if (!clienteDAO.findById(id).isPresent()) {
            throw new RuntimeException("Cliente não encontrado com ID: " + id);
        }
        clienteDAO.deleteById(id);
    }

    public boolean verificarEmailExiste(String email) {
        return clienteDAO.existsByEmail(email);
    }
}
