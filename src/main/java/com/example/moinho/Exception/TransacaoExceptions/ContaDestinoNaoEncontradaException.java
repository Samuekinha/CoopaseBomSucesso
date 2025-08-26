package com.example.moinho.Exception.TransacaoExceptions;

import com.example.moinho.Exception.BusinessException;

public class ContaDestinoNaoEncontradaException extends BusinessException {

    public ContaDestinoNaoEncontradaException(String message) {
        super(message);
    }

    public ContaDestinoNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
