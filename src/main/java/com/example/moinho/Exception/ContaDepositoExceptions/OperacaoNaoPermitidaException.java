package com.example.moinho.Exception.ContaDepositoExceptions;

import com.example.moinho.Exception.BusinessException;

public class OperacaoNaoPermitidaException extends BusinessException {

    public OperacaoNaoPermitidaException(String message) {
        super(message, "/Coopase/Conta/Servicos");
    }
}
