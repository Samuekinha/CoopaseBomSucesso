package com.example.moinho.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransacaoRequest {

    @NotNull(message = "O tipo da transação é obrigatório")
    private String tipoTransacao;

    @NotNull(message = "O valor da transação é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valorTransacao;

    @NotNull(message = "A conta destino é obrigatória")
    private Long contaDestinoId;

    private Long contaOrigemId;

    @NotNull(message = "O operador da transação é obrigatório")
    private Long operadorTransacaoId;

    @NotNull(message = "A forma da transação é obrigatória")
    private String formaDinheiroTransacao;

    private String descricaoTransacao;
}
