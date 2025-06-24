package com.example.moinho.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "deposito_transacoes") // Define o nome da tabela no banco
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
        ASSOCIADOS,
        MATERIA_PRIMA,
        OUTROS
    }

    @Enumerated(EnumType.STRING)  // Armazena o nome do enum como texto
    @Column(length = 40)
    private E_Despesas.TypeExchange typeExchange;

    @ManyToOne
    @JoinColumn(name = "name")
    private E_Cliente third_parties; // Data que foi feito o processo (caso seja ultrajada)

    @Column(nullable = false)
    private LocalDate process_date; // Data que foi feito o processo (caso seja ultrajada)

    @Column(length = 60, nullable = false)
    private String description; // Descrição (motivo)
}
