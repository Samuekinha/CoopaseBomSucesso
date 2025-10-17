package com.example.moinho.Entity.Pessoa;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pessoa_base")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_entidade", discriminatorType = DiscriminatorType.STRING)
@Data
public abstract class PessoaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String nome;
}
