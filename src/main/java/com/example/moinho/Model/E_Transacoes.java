package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposito_transacoes") // Define o nome da tabela no banco
@Data
public class E_Transacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeTransaction { // Define as opções do ENUM
        DEPOSIT,
        WITHDRAW,
        TRANSFER
    }

    @Enumerated(EnumType.STRING)  // Armazena o nome do enum como texto
    @Column(length = 20)
    private TypeTransaction typeTransaction;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal value; // Valor da transação

    @ManyToOne
    @JoinColumn(name = "name")  // Nome da coluna FK
    private E_ContaDeposito vault_id;

    @ManyToOne
    @JoinColumn(name = "name_client")  // Nome da coluna FK
    private E_Cliente translaction_operator;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime time; // Define um timestamp pro momento da transação

}