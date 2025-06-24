package com.example.moinho.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Coopase")
public class C_TelaInicial {

    @GetMapping("/TelaInicial")
    public String telaInicial() {
        // Retorna a tela inicial (HTML)
        return "Coopase/TelaInicial";
    }
}