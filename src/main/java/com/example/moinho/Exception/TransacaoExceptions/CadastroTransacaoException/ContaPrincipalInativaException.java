package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class ContaPrincipalInativaException extends BusinessException {
    public ContaPrincipalInativaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
