package com.example.moinho.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposito_transacoes") // Define o nome da tabela no banco
@Data
public class E_DepositoCaixaTransacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeTransaction { // Define as opções do ENUM
        DEPOSIT,
        WITHDRAW
    }

    @Enumerated(EnumType.STRING)  // Armazena o nome do enum como texto
    @Column(length = 20)
    private TypeTransaction type;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id")  // Nome da coluna FK
    private E_DepositoCaixa vault_id;

    @ManyToOne
    @JoinColumn(name = "name")  // Nome da coluna FK
    private E_Cliente translaction_operator;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    @Column(length = 40, nullable = false)
    private String description;

}