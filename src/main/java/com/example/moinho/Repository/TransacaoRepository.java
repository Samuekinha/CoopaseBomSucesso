package com.example.moinho.Repository;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Model.TransacaoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransacaoRepository extends JpaRepository<TransacaoTable, Long> {

    @Query("""
        SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
            t.id,
            cd.vaultName,
            coalesce(cd2.vaultName, ''),
            op.name,
            t.typeTransaction,
            t.value,
            t.typeMoney,
            t.dataTransacao,
            t.descricao,
            t.saldoAnterior,
            t.saldoPosterior,
            t.ativa
        )
        FROM TransacaoTable t
        LEFT JOIN t.contaDeposito cd
        LEFT JOIN t.contaDestino cd2
        LEFT JOIN t.operador op
    """)
    List<TransacaoResumoDTO> buscarResumo();

    @Query("""
        SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
            t.id,
            cd.vaultName,
            coalesce(cd2.vaultName, ''),
            op.name,
            t.typeTransaction,
            t.value,
            t.typeMoney,
            t.dataTransacao,
            t.descricao,
            t.saldoAnterior,
            t.saldoPosterior,
            t.ativa
        )
        FROM TransacaoTable t
        LEFT JOIN t.contaDeposito cd
        LEFT JOIN t.contaDestino cd2
        LEFT JOIN t.operador op
        WHERE t.ativa = false
    """)
    List<TransacaoResumoDTO> buscarResumoInativas();

    @Query("""
        SELECT new com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO(
            t.id,
            cd.vaultName,
            coalesce(cd2.vaultName, ''),
            op.name,
            t.typeTransaction,
            t.value,
            t.typeMoney,
            t.dataTransacao,
            t.descricao,
            t.saldoAnterior,
            t.saldoPosterior,
            t.ativa
        )
        FROM TransacaoTable t
        LEFT JOIN t.contaDeposito cd
        LEFT JOIN t.contaDestino cd2
        LEFT JOIN t.operador op
        WHERE t.ativa = true
    """)
    List<TransacaoResumoDTO> buscarResumoAtivas();

}