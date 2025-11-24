package com.example.moinho.Exception.ContaDepositoExceptions.Cadastro;

import com.example.moinho.Exception.BusinessException;

public class NomeInvalidoException extends BusinessException {

    public NomeInvalidoException(String message) {
        super(message, "redirect:/Coopase/Servicos/ContaDeposito");
    }
}
