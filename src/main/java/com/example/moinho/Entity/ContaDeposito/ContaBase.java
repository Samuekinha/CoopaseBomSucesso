package com.example.moinho.Entity.ContaDeposito;

import com.example.moinho.Entity.Transacao;
import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.SaldoInsuficienteException;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta_base")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
@Data
public abstract class ContaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String nome_conta;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal valor_total = BigDecimal.ZERO;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime data_criacao;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean ativa = true;

    public void aplicarTransacao(BigDecimal valor, Transacao.TypeTransaction tipoTransacao) {
        if (valor == null) throw new IllegalArgumentException("Valor nulo");

        switch (tipoTransacao) {
            case DEPOSITO -> this.valor_total = this.valor_total.add(valor);
            case SAQUE -> {
                if (this.valor_total.compareTo(valor) < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente");
                }
                this.valor_total = this.valor_total.subtract(valor);
            }
        }
    }
}
