package com.example.moinho.Entity.Pessoa;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("FISICA")
@Data
public class PessoaFisica extends PessoaBase {

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(unique = true, length = 14)
    private String rg;

    @Column(columnDefinition = "DATE")
    private LocalDate data_nascimento;

    @Column(length = 50, nullable = true)
    private String caf_fisica;

    @Column(columnDefinition = "DATE", nullable = true)
    private LocalDate validade_caf_fisica;

    @ElementCollection(targetClass = Papel.class)
    @CollectionTable(name = "pessoa_fisica_papeis", joinColumns = @JoinColumn(name = "pessoa_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "papel")
    private Set<Papel> papeis = new HashSet<>();
}
