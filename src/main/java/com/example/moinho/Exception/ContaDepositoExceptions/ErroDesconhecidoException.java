package com.example.moinho.Exception.ContaDepositoExceptions;

import com.example.moinho.Exception.BusinessException;

public class ErroDesconhecidoException extends BusinessException {

    public ErroDesconhecidoException(String message) {
        super(message, "redirect:/Coopase/Conta/Servicos");
    }
}
