package com.example.sistemadesalgado.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "itens_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salgado_id", nullable = false)
    private SalgadoEstoque salgadoEstoque;

    @Column(nullable = false, length = 100)
    private String sabor;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Double valorUnitario;

    @PrePersist
    protected void onCreate() {
        if (sabor == null && salgadoEstoque != null) {
            sabor = salgadoEstoque.getSabor();
        }
    }
}
