package com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions;

import com.example.moinho.Exception.BusinessException;

public class IdInvalidoException extends BusinessException {
    public IdInvalidoException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
