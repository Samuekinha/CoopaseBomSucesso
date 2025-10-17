package com.example.moinho.Dto.Transacao;

import com.example.moinho.Entity.Transacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransacaoRequestDTO {

    @NotNull(message = "O tipo da transação é obrigatório")
    private Transacao.TypeTransaction tipoTransacao;

    @NotNull(message = "O valor da transação é obrigatório")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    private BigDecimal valorTransacao;

    private Long contaDestinoId;

    // conta principal
    @NotNull(message = "A conta origem/principal é obrigatória")
    private Long contaPrincipalId;

    @NotNull(message = "O operador da transação é obrigatório")
    private Long operadorTransacaoId;

    @NotNull(message = "A forma da transação é obrigatória")
    private Transacao.TypeMoney formaDinheiroTransacao;

    private String descricaoTransacao;
}
