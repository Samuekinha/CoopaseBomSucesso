package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposito_dinheiro") // Define o nome da tabela no banco
@Data
public class E_DepositoDinheiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 25)
    private String vault_name;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal total_amount;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime creation_date; // Tempo exato que foi criado
}