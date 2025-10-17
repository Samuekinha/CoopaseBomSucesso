package com.example.moinho.Dto.Transacao.Resumo;

import com.example.moinho.Entity.Transacao;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TransacaoResumoDTO {
    private Long id;
    private String conta_principal_nome;
    private String conta_destino_nome;
    private String operador_nome;
    private Transacao.TypeTransaction tipo_transacao;
    private BigDecimal valor;
    private Transacao.TypeMoney tipo_dinheiro;
    private LocalDateTime data;
    private String descricao;
    private BigDecimal saldo_anterior;
    private BigDecimal saldo_posterior;
    private boolean ativa;
    private boolean automatica; // se é automatica ou não (manual)

    public TransacaoResumoDTO(
            Long id,
            String conta_principal_nome,
            String conta_destino_nome,
            String operador_nome,
            Transacao.TypeTransaction tipo_transacao,
            BigDecimal valor,
            Transacao.TypeMoney tipo_dinheiro,
            LocalDateTime data,
            String descricao,
            BigDecimal saldo_anterior,
            BigDecimal saldo_posterior,
            boolean ativa,
            boolean automatica
    ) {
        this.id = id;
        this.conta_principal_nome = conta_principal_nome;
        this.conta_destino_nome = conta_destino_nome;
        this.operador_nome = operador_nome;
        this.tipo_transacao = tipo_transacao;
        this.valor = valor;
        this.tipo_dinheiro = tipo_dinheiro;
        this.data = data;
        this.descricao = descricao;
        this.saldo_anterior = saldo_anterior;
        this.saldo_posterior = saldo_posterior;
        this.ativa = ativa;
        this.automatica = automatica;
    }
}
