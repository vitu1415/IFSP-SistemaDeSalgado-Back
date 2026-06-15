package com.example.sistemadesalgado.service;

import com.example.sistemadesalgado.dao.ClienteDAO;
import com.example.sistemadesalgado.exception.BusinessException;
import com.example.sistemadesalgado.exception.ResourceNotFoundException;
import com.example.sistemadesalgado.model.dto.LoginRequest;
import com.example.sistemadesalgado.model.dto.LoginResponse;
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
            throw new BusinessException("Email já cadastrado: " + cliente.getEmail());
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
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
            clienteDAO.existsByEmail(clienteAtualizado.getEmail())) {
            throw new BusinessException("Email já cadastrado: " + clienteAtualizado.getEmail());
        }

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setSenha(clienteAtualizado.getSenha());

        return clienteDAO.update(clienteExistente);
    }

    public LoginResponse autenticar(LoginRequest loginRequest) {
        var clienteOpt = buscarPorEmail(loginRequest.getEmail());

        if (clienteOpt.isEmpty()) {
            return new LoginResponse(null, null, null, "Email não encontrado");
        }

        var cliente = clienteOpt.get();

        if (!cliente.getSenha().equals(loginRequest.getSenha())) {
            return new LoginResponse(null, null, null, "Senha incorreta");
        }

        return new LoginResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                "Login realizado com sucesso"
        );
    }
}
