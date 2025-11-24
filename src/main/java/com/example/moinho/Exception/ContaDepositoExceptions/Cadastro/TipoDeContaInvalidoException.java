package com.example.moinho.Exception.ContaDepositoExceptions.Cadastro;

import com.example.moinho.Exception.BusinessException;

public class TipoDeContaInvalidoException extends BusinessException {

    public TipoDeContaInvalidoException(String message) {
        super(message, "redirect:/Coopase/Servicos/ContaDeposito");
    }
}
