package com.example.moinho.Exception.DiaristaException.CadastroDiaristaException;

import com.example.moinho.Exception.BusinessException;

public class DiaristaJaCadastradoException extends BusinessException {
    public DiaristaJaCadastradoException(String message) {
        super(message, "redirect:/Coopase/Servicos/Diarista");
    }
}
