package com.example.moinho.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "despesas") // Define o nome da tabela no banco
@Data
public class E_Despesas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum TypeExchange { // Define as opções do ENUM
        FUNCIONARIO,
        COMBUSTIVEIS,
        MATERIAIS_DE_ESCRITORIO,
        PECAS_E_MATERAIS,
        MAQUINAS,
        TAXAS_OU_IMPOSTOS,
        AGUA_E_ESGOTO,
        LUZ_E_ENERGIA,
        ACESSORIA_CONTRIBUICAO_E_CONTABILIDADE,
        ASSOCIADOS
    }

    @Enumerated(EnumType.STRING)  // Armazena o nome do enum como texto
    @Column(length = 20)
    private TypeExchange type;

    @Column(columnDefinition = "DECIMAL(10,3)")
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id")  // Nome da coluna FK
    private E_DepositoCaixa vault_id;

    @ManyToOne
    @JoinColumn(name = "name")  // Nome da coluna FK
    private E_DepositoCaixa translaction_operator;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime time;

    @Column(length = 40, nullable = false)
    private String description;

}