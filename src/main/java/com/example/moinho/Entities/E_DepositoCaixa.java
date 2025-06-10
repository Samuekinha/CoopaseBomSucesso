package com.example.moinho.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "cofre") // Define o nome da tabela no banco
@Data
public class E_DepositoCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String vault_name;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal total_amount;
}