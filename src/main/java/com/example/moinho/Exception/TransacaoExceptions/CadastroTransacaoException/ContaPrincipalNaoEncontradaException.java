package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class ContaPrincipalNaoEncontradaException extends BusinessException {

    public ContaPrincipalNaoEncontradaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
