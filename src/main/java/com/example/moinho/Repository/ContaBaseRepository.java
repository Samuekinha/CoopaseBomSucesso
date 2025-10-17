package com.example.moinho.Repository;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContaBaseRepository extends JpaRepository<ContaBase, Long> {

    // OU, se quiser usar o nome da coluna explicitamente:
    @Query("SELECT c FROM ContaBase c WHERE c.nome_conta = :nome_conta")
    Optional<ContaBase> findPorNome(@Param("nome_conta") String nome_conta);

    @Query(value = "SELECT * FROM conta_base", nativeQuery = true)
    List<ContaBase> findTodasContaDeposito();
}