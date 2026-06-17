package com.example.sistemadesalgado.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "salgadosEstoque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalgadoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String sabor;

    @Column(nullable = false)
    private Double preco;

    @Column(nullable = false)
    private Integer estoque;

    @OneToMany(mappedBy = "salgadoEstoque", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (estoque == null) {
            estoque = 0;
        }
    }
}
