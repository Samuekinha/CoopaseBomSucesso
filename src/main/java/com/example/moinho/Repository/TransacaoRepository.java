package com.example.moinho.Repository;

import com.example.moinho.Model.TransacaoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransacaoRepository extends JpaRepository<TransacaoTable, Long> {

    // OU, se quiser usar o nome da coluna explicitamente:
    @Query("SELECT c FROM TransacaoTable ORDER BY id")
    List<TransacaoTable> findAll();

}