package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class ContaDestinoNaoEncontradaException extends BusinessException {

    public ContaDestinoNaoEncontradaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
