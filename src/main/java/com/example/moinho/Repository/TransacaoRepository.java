package com.example.moinho.Repository;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query(value = """
        SELECT DISTINCT conta_principal_id AS conta_id FROM transacao
        UNION
        SELECT DISTINCT conta_destino_id AS conta_id FROM transacao
    """, nativeQuery = true)
    List<Long> findAllContasUsadas();

    @Query("""
    SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
        t.id,
        cd.nome_conta,
        cd2.nome_conta,
        op.nome,
        t.tipo_transacao,
        t.valor,
        t.tipo_dinheiro,
        t.data_transacao,
        t.descricao,
        t.saldo_anterior,
        t.saldo_posterior,
        t.ativa,
        t.automatica
    )
    FROM Transacao t
    LEFT JOIN t.conta_principal cd
    LEFT JOIN t.conta_destino cd2
    LEFT JOIN t.operador op
""")
    List<TransacaoResumoDTO> buscarResumo();


    @Query("""
    SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
        t.id,
        cd.nome_conta,
        cd2.nome_conta,
        op.nome,
        t.tipo_transacao,
        t.valor,
        t.tipo_dinheiro,
        t.data_transacao,
        t.descricao,
        t.saldo_anterior,
        t.saldo_posterior,
        t.ativa,
        t.automatica
    )
    FROM Transacao t
    LEFT JOIN t.conta_principal cd
    LEFT JOIN t.conta_destino cd2
    LEFT JOIN t.operador op
    WHERE t.ativa = false
""")
    List<TransacaoResumoDTO> buscarResumoInativas();


    @Query("""
    SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
        t.id,
        cd.nome_conta,
        cd2.nome_conta,
        op.nome,
        t.tipo_transacao,
        t.valor,
        t.tipo_dinheiro,
        t.data_transacao,
        t.descricao,
        t.saldo_anterior,
        t.saldo_posterior,
        t.ativa,
        t.automatica
    )
    FROM Transacao t
    LEFT JOIN t.conta_principal cd
    LEFT JOIN t.conta_destino cd2
    LEFT JOIN t.operador op
    WHERE t.ativa = true
""")
    List<TransacaoResumoDTO> buscarResumoAtivas();
}