package com.example.moinho.Repository;

import com.example.moinho.Entities.E_Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface R_Cliente extends JpaRepository<E_Cliente, Long> {
}
