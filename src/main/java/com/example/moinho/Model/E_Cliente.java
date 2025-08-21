package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cliente") // Define o nome da tabela no banco
@Data
public class E_Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String name;

    @Column(unique=true, length = 14)
    private String document; // CPF ou CNPJ

    @Column(columnDefinition = "DECIMAL(10,3)")
    private Double balance_kg;

    @Column(columnDefinition = "DECIMAL(19,4)", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean cooperated;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean seller;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean operator;

    @Column(unique = true, length = 50)
    private String caf;

    @Column(columnDefinition = "DATE")
    private LocalDate maturity_caf;

    // Construtor que garante balance nunca será null
    public void Balance() {
        this.balance = BigDecimal.ZERO;
    }

}