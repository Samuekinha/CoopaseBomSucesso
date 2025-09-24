package com.example.moinho.Dto.Transacao.Resumo;

import com.example.moinho.Model.TransacaoTable;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TransacaoResumoDTO {
    private Long id;
    private String contaDepositoNome;
    private String contaDestinoNome;
    private String operadorName;
    private TransacaoTable.TypeTransaction tipoTransacao;
    private BigDecimal valor;
    private TransacaoTable.TypeMoney tipoDinheiro;
    private LocalDateTime data;
    private String descricao;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoPosterior;
    private boolean ativa;

    public TransacaoResumoDTO(
            Long id,
            String contaDepositoNome,
            String contaDestinoNome,
            String operadorName,
            TransacaoTable.TypeTransaction tipoTransacao,
            BigDecimal valor,
            TransacaoTable.TypeMoney tipoDinheiro,
            LocalDateTime data,
            String descricao,
            BigDecimal saldoAnterior,
            BigDecimal saldoPosterior,
            boolean ativa
    ) {
        this.id = id;
        this.contaDepositoNome = contaDepositoNome;
        this.contaDestinoNome = contaDestinoNome;
        this.operadorName = operadorName;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.tipoDinheiro = tipoDinheiro;
        this.data = data;
        this.descricao = descricao;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.ativa = ativa;
    }
}
