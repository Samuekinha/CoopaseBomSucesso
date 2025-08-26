package com.example.moinho.Exception.TransacaoExceptions;

import com.example.moinho.Exception.BusinessException;

public class ContaDestinoInativaException extends BusinessException {
    public ContaDestinoInativaException(String message) {
        super(message);
    }

    public ContaDestinoInativaException(String message, Throwable cause) {
        super(message, cause);
    }
}
