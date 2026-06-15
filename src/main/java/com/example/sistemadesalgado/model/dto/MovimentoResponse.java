package com.example.sistemadesalgado.model.dto;

import com.example.sistemadesalgado.model.enums.TipoMovimento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimentoResponse {
    private Long id;
    private TipoMovimento tipoMovimento;
    private Double valor;
    private LocalDateTime dataHora;
}
