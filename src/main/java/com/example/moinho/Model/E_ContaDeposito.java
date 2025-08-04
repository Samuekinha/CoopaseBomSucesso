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
    private LocalDateTime creation_date; // Tempo exato que foi criado
}