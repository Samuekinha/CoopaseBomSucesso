package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Data
public class TransacaoTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeTransaction {
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeTransaction typeTransaction;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal value;

    public enum TypeMoney {
        PIX,
        DINHEIRO,
        CHEQUE
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeMoney typeMoney;

    @ManyToOne
    @JoinColumn(name = "conta_deposito_id", referencedColumnName = "id")  // Correct FK reference
    private E_ContaDeposito contaDeposito;  // Renamed to follow conventions

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")  // Correct FK reference
    private E_Cliente operador;  // Renamed to follow conventions -> alterar de cliente para Operador

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime dataTransacao;  // Renamed to Portuguese for consistency

    // Additional improvements you might consider:

    @Column(length = 255)
    private String descricao;  // Optional: description of the transaction

    @Column(name = "saldo_anterior", columnDefinition = "DECIMAL(10,3)")
    private BigDecimal saldoAnterior;  // Optional: balance before transaction

    @Column(name = "saldo_posterior", columnDefinition = "DECIMAL(10,3)")
    private BigDecimal saldoPosterior;  // Optional: balance after transaction

    public BigDecimal calcularSaldoPosterior(BigDecimal saldoAtual, BigDecimal valorTransacao) {
        if (valorTransacao == null || valorTransacao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor inválido para depósito.");
        }
        return saldoAtual.add(valorTransacao);
    }
}