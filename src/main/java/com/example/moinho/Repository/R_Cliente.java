package com.example.moinho.Repository;

import com.example.moinho.Model.E_Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface R_Cliente extends JpaRepository<E_Cliente, Long> {

    Optional<E_Cliente> findByDocument(String document);

    Optional<E_Cliente> findByName(String name);

    @Query(value = "SELECT * FROM cliente ORDER BY id", nativeQuery = true)
    List<E_Cliente> findAllOrderById();

    @Query(value = "SELECT * FROM cliente WHERE cooperated = true ORDER BY id", nativeQuery = true)
    List<E_Cliente> findAllCooperateds();

    @Query(value = "SELECT * FROM cliente WHERE cooperated = true ORDER BY id LIMIT ?1", nativeQuery = true)
    List<E_Cliente> findCooperatedsLimited(int limit);

    @Query(value = "SELECT * FROM cliente WHERE seller = true ORDER BY id LIMIT ?1", nativeQuery = true)
    List<E_Cliente> findSellersLimited(int limit);

}
