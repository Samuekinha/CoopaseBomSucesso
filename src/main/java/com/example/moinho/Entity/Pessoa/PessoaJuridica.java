package com.example.moinho.Entity.Pessoa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import java.time.LocalDate;
import lombok.Data;

@Entity
@DiscriminatorValue("JURIDICA")
@Data
public class PessoaJuridica extends PessoaBase {

    @Column(unique = true, length = 14)
    private String cnpj;

    @Column(length = 50, nullable = true)
    private String caf_juridica;

    @Column(columnDefinition = "DATE", nullable = true)
    private LocalDate validade_caf_juridica;
}
