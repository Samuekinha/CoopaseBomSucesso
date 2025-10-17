package com.example.moinho.Entity;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Entity.Pessoa.PessoaBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Getter
@Setter
@ToString(exclude = {"conta_principal", "conta_destino", "operador"})
@EqualsAndHashCode
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeTransaction {
        DEPOSITO,
        SAQUE,
        TRANSFERENCIA
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeTransaction tipo_transacao;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal valor;

    public enum TypeMoney {
        PIX,
        DINHEIRO,
        CHEQUE,
        MOEDA
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeMoney tipo_dinheiro;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")  // Correct FK reference
    private ContaBase conta_principal;  // conta principal origem

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")  // Correct FK reference
    private ContaBase conta_destino;  // conta apenas para transacao

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")  // Correct FK reference
    private PessoaBase operador;  // Renamed to follow conventions -> alterar de cliente para Operador

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime data_transacao;  // Renamed to Portuguese for consistency

    // Additional improvements you might consider:

    @Column(length = 255)
    private String descricao;  // Optional: description of the transaction

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal saldo_anterior;  // Optional: balance before transaction

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal saldo_posterior;  // Optional: balance after transaction

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean ativa = true;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean automatica = false; // transação manual ou automatica

}

