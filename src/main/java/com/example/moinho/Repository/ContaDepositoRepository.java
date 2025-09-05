package com.example.moinho.Repository;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContaDepositoRepository extends JpaRepository<E_ContaDeposito, Long> {

    // OU, se quiser usar o nome da coluna explicitamente:
    @Query("SELECT c FROM E_ContaDeposito c WHERE c.vaultName = :vaultName")
    Optional<E_ContaDeposito> findByVaultName(@Param("vaultName") String vaultName);

    @Query(value = "SELECT * FROM conta_deposito", nativeQuery = true)
    List<E_ContaDeposito> findAllContasD();
}