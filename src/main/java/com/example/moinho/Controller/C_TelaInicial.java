package com.example.moinho.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class C_TelaInicial {

    @GetMapping("/Coopase")
    public String coopaseRedirecionamento() {
        return "redirect:/Coopase/TelaInicial";
    }

    @GetMapping("/Coopase/TelaInicial")
    public String telaInicial() {
        // Retorna a tela inicial (HTML)
        return "Coopase/TelaInicial";
    }
}