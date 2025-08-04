package com.example.moinho.Repository;

import com.example.moinho.Model.E_ContaDeposito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface R_ContaDeposito extends JpaRepository<E_ContaDeposito, Long> {

    Optional<E_ContaDeposito> findByvault_name(String vault_name);
}