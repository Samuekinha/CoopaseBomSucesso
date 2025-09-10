package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class OperadorNaoEncontradoException extends BusinessException {

    public OperadorNaoEncontradoException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }

}
