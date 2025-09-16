package com.example.moinho.Exception.TransacaoExceptions.InativarExceptions;

import com.example.moinho.Exception.BusinessException;

public class TransacaoInativaException extends BusinessException {
    public TransacaoInativaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
