package com.example.sistemadesalgado.controller;

import com.example.sistemadesalgado.model.dto.HistoricoResponse;
import com.example.sistemadesalgado.model.dto.LoginRequest;
import com.example.sistemadesalgado.model.dto.LoginResponse;
import com.example.sistemadesalgado.service.ClienteService;
import com.example.sistemadesalgado.service.HistoricoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final HistoricoService historicoService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = clienteService.autenticar(loginRequest);

        if (response.getId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<HistoricoResponse> buscarHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(historicoService.buscarHistoricoCliente(id));
    }
}
