package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class SaldoInsuficienteException extends BusinessException {

    public SaldoInsuficienteException(String message) {
            super(message, "/Coopase/Servicos/Transacao");
    }

}


