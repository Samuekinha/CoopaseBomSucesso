package com.example.moinho.Exception.ContaDepositoExceptions.Delete;

import com.example.moinho.Exception.BusinessException;

public class ContaNaoEncontradaException extends BusinessException {

    public ContaNaoEncontradaException(String message) {
        super(message, "redirect:/Coopase/Conta/Servicos");
    }
}
