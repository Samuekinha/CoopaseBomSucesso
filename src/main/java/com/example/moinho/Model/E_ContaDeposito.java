package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta_deposito") // Define o nome da tabela no banco
@Data
public class E_ContaDeposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vault_name", nullable = false, length = 40)
    private String vaultName;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal total_amount;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creation_date;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean active = true;

    // Aplica um depósito (adiciona ao total)
    public void aplicarDeposito(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero");
        }
        this.total_amount = this.total_amount.add(valor);
    }

    // Aplica um desconto ou retirada (subtrai do total)
    public void aplicarDesconto(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor do desconto deve ser maior que zero");
        }
        if (this.total_amount.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para aplicar o desconto");
        }
        this.total_amount = this.total_amount.subtract(valor);
    }
}
