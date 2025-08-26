package com.example.moinho.Exception.TransacaoExceptions;

import com.example.moinho.Exception.BusinessException;

public class OperadorNaoEncontradoException extends BusinessException {

    public OperadorNaoEncontradoException(String message) {
        super(message);
    }

    public OperadorNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}
