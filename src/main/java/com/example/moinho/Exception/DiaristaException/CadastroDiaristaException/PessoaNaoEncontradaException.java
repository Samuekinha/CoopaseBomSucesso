package com.example.moinho.Exception.DiaristaException.CadastroDiaristaException;

import com.example.moinho.Exception.BusinessException;

public class PessoaNaoEncontradaException extends BusinessException {
    public PessoaNaoEncontradaException(String message) {
        super(message, "redirect:/Coopase/Servicos/Diarista");
    }
}
