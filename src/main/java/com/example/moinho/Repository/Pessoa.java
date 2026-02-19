package com.example.moinho.Repository;

import com.example.moinho.Entity.Pessoa.Papel;
import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Entity.Pessoa.PessoaJuridica;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Pessoa extends JpaRepository<PessoaBase, Long> {

    @Query("SELECT p FROM PessoaFisica p WHERE p.cpf = :cpf")
    Optional<PessoaFisica> findByCpf(@Param("cpf") String cpf);

    @Query("SELECT p FROM PessoaJuridica p WHERE p.cnpj = :cnpj")
    Optional<PessoaJuridica> findByCnpj(@Param("cnpj") String cnpj);

    Optional<PessoaBase> findByNome(String nome);

    @Query(value = "SELECT * FROM pessoa_base ORDER BY nome", nativeQuery = true)
    List<PessoaBase> findTodosOrdenadosPorNome();

    @Query("""
        SELECT p FROM PessoaFisica p
        JOIN p.papeis papel
        WHERE papel = :papel
    """)
    List<PessoaFisica> findPorPapel(@Param("papel") Papel papel);

    @Query("""
    SELECT p FROM PessoaFisica p
    WHERE :papel NOT MEMBER OF p.papeis
""")
    List<PessoaFisica> findSemPapel(@Param("papel") Papel papel, Pageable pageable);

    @Query("""
        SELECT p FROM PessoaFisica p
        JOIN p.papeis papel
        WHERE papel = :papel
    """)
    List<PessoaFisica> findPorPapelLimitados(@Param("papel") Papel papel, Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM PessoaFisica p
        LEFT JOIN p.papeis papel
        WHERE (:pesquisaPorNome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :pesquisaPorNome, '%')))
        AND (:pesquisaPorCpf IS NULL OR p.cpf LIKE CONCAT('%', :pesquisaPorCpf, '%'))
        AND (:pesquisaPorCodCaf IS NULL OR p.caf_fisica LIKE CONCAT('%', :pesquisaPorCodCaf, '%'))
        AND (
            :apenasCooperados IS NULL 
            OR (:apenasCooperados = TRUE AND papel = com.example.moinho.Entity.Pessoa.Papel.COOPERADO)
            OR (:apenasCooperados = FALSE)
        )
        ORDER BY p.nome ASC
    """)
    List<PessoaFisica> findTodosComFiltrosAsc(
            @Param("pesquisaPorNome") String pesquisaPorNome,
            @Param("pesquisaPorCpf") String pesquisaPorCpf,
            @Param("pesquisaPorCodCaf") String pesquisaPorCodCaf,
            @Param("apenasCooperados") Boolean apenasCooperados
    );

    @Query("""
        SELECT DISTINCT p FROM PessoaFisica p
        LEFT JOIN p.papeis papel
        WHERE (:pesquisaPorNome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :pesquisaPorNome, '%')))
        AND (:pesquisaPorCpf IS NULL OR p.cpf LIKE CONCAT('%', :pesquisaPorCpf, '%'))
        AND (:pesquisaPorCodCaf IS NULL OR p.caf_fisica LIKE CONCAT('%', :pesquisaPorCodCaf, '%'))
        AND (
            :apenasCooperados IS NULL 
            OR (:apenasCooperados = TRUE AND papel = com.example.moinho.Entity.Pessoa.Papel.COOPERADO)
            OR (:apenasCooperados = FALSE)
        )
        ORDER BY p.nome DESC
    """)
    List<PessoaFisica> findTodosComFiltrosDesc(
            @Param("pesquisaPorNome") String pesquisaPorNome,
            @Param("pesquisaPorCpf") String pesquisaPorCpf,
            @Param("pesquisaPorCodCaf") String pesquisaPorCodCaf,
            @Param("apenasCooperados") Boolean apenasCooperados
    );
}
