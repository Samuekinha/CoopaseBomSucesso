package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class TipoTransacaoInexistenteException extends BusinessException {

    public TipoTransacaoInexistenteException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }

}

