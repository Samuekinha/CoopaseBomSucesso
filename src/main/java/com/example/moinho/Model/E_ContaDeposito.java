package com.example.moinho.Model;

import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.SaldoInsuficienteException;
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

    public void aplicarTransacao(BigDecimal valor, TransacaoTable.TypeTransaction tipoTransacao) {
        if (valor == null) throw new IllegalArgumentException("Valor nulo");

        switch (tipoTransacao) {
            case DEPOSIT -> this.total_amount = this.total_amount.add(valor);
            case WITHDRAW -> {
                if (this.total_amount.compareTo(valor) < 0) {
                    throw new SaldoInsuficienteException("Saldo insuficiente");
                }
                this.total_amount = this.total_amount.subtract(valor);
            }
        }
    }

}
