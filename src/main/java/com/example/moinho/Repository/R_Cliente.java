package com.example.moinho.Repository;

import com.example.moinho.Model.E_Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface R_Cliente extends JpaRepository<E_Cliente, Long> {

    Optional<E_Cliente> findByDocument(String document);

    Optional<E_Cliente> findByName(String name);

    @Query(value = "SELECT * FROM cliente " +
            "ORDER BY name", nativeQuery = true)
    List<E_Cliente> findAllOrderById();

    @Query(value = "SELECT * FROM cliente " +
            "WHERE operator is TRUE", nativeQuery = true)
    List<E_Cliente> findAllOperadores();

    @Query(nativeQuery = true, value = """
    SELECT * FROM cliente 
    WHERE (:pesquisaPorNome IS NULL OR name ILIKE CONCAT('%', :pesquisaPorNome, '%'))
    AND (:pesquisaPorDocumento IS NULL OR document ILIKE CONCAT('%', :pesquisaPorDocumento, '%'))
    AND (:pesquisaPorCodCaf IS NULL OR caf ILIKE CONCAT('%', :pesquisaPorCodCaf, '%'))
    AND (:apenasCooperados IS NULL OR cooperated = CAST(:apenasCooperados AS BOOLEAN))
    ORDER BY name ASC
    """)
    List<E_Cliente> findAllWithFiltersAsc(
            @Param("pesquisaPorNome") String pesquisaPorNome,
            @Param("pesquisaPorDocumento") String pesquisaPorDocumento,
            @Param("pesquisaPorCodCaf") String pesquisaPorCodCaf,
            @Param("apenasCooperados") Boolean apenasCooperados);

    @Query(nativeQuery = true, value = """
    SELECT * FROM cliente 
    WHERE (:pesquisaPorNome IS NULL OR name ILIKE CONCAT('%', :pesquisaPorNome, '%'))
    AND (:pesquisaPorDocumento IS NULL OR document ILIKE CONCAT('%', :pesquisaPorDocumento, '%'))
    AND (:pesquisaPorCodCaf IS NULL OR caf ILIKE CONCAT('%', :pesquisaPorCodCaf, '%'))
    AND (:apenasCooperados IS NULL OR cooperated = CAST(:apenasCooperados AS BOOLEAN))
    ORDER BY name DESC
    """)
    List<E_Cliente> findAllWithFiltersDesc(
            @Param("pesquisaPorNome") String pesquisaPorNome,
            @Param("pesquisaPorDocumento") String pesquisaPorDocumento,
            @Param("pesquisaPorCodCaf") String pesquisaPorCodCaf,
            @Param("apenasCooperados") Boolean apenasCooperados);

    @Query(value = "SELECT * FROM cliente " +
            "WHERE cooperated = true " +
            "ORDER BY name", nativeQuery = true)
    List<E_Cliente> findAllCooperateds();

    @Query(value = "SELECT * FROM cliente " +
            "WHERE cooperated = true " +
            "ORDER BY name LIMIT ?1", nativeQuery = true)
    List<E_Cliente> findCooperatedsLimited(int limit);

    @Query(value = "SELECT * FROM cliente " +
            "WHERE seller = true " +
            "ORDER BY name LIMIT ?1", nativeQuery = true)
    List<E_Cliente> findSellersLimited(int limit);


    @Query(value = "SELECT * FROM cliente " +
            "WHERE seller = true " +
            "ORDER BY name", nativeQuery = true)
    List<E_Cliente> findSellers();

}
