package com.example.moinho.Repository;

import com.example.moinho.Model.E_Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface R_Cliente extends JpaRepository<E_Cliente, Long> {

    Optional<E_Cliente> findByDocument(String document);

    Optional<E_Cliente> findByName(String name);

    List<E_Cliente> findByCooperatedTrue();
}
