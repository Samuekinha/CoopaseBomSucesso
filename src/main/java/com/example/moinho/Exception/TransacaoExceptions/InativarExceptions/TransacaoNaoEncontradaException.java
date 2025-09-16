package com.example.moinho.Exception.TransacaoExceptions.InativarExceptions;

import com.example.moinho.Exception.BusinessException;

public class TransacaoNaoEncontradaException extends BusinessException {
    public TransacaoNaoEncontradaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
