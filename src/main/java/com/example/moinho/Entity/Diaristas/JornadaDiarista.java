package com.example.moinho.Entity.Diaristas;

import com.example.moinho.Entity.Pessoa.PessoaFisica;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "jornada_diarista")
@Data
public class JornadaDiarista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diarista_id", nullable = false)
    private PessoaFisica diarista;

    @Column(nullable = false)
    private LocalDate dia;

    private LocalTime horaEntradaManha;

    private LocalTime horaSaidaManha;

    private LocalTime horaEntradaTarde;

    private LocalTime horaSaidaTarde;

    private String observacao;
}