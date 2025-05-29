package com.example.moinho.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clientes") // Define o nome da tabela no banco
@Data
public class E_Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    @Column(unique = true, length = 14)
    private String documento;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private double balance_kg;

    @Column(columnDefinition = "DECIMAL(19,4)", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(columnDefinition = "BOOLEAN", nullable = false)
    private boolean cooperated;

    @Column(unique = true, length = 40)
    private String caf;

    @Column(columnDefinition = "DATE")
    private LocalDate maturity_caf;

    // Construtor que garante balance nunca será null
    public void Balance() {
        this.balance = BigDecimal.ZERO;
    }

}