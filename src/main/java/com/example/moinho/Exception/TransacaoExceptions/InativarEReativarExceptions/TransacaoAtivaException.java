package com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions;

import com.example.moinho.Exception.BusinessException;

public class TransacaoAtivaException extends BusinessException {

    public TransacaoAtivaException(String message) {
            super(message, "/Coopase/Servicos/Transacao");
    }

}


