package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class OperadorSemPermissaoException extends BusinessException {

    public OperadorSemPermissaoException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }

}

