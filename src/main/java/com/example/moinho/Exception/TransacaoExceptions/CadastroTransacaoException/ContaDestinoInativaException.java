package com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException;

import com.example.moinho.Exception.BusinessException;

public class ContaDestinoInativaException extends BusinessException {
    public ContaDestinoInativaException(String message) {
        super(message, "/Coopase/Servicos/Transacao");
    }
}
