package com.example.moinho.Repository;

import com.example.moinho.Model.TransacaoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransacaoRepository extends JpaRepository<TransacaoTable, Long> {

    @Query(value = "SELECT * FROM transacao", nativeQuery = true)
    List<TransacaoTable> findAll();

}