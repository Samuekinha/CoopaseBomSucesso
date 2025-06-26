package com.example.moinho.Controller.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Coopase/Cliente/Servicos")
public class CadastrarClienteController {

    // Rotas para processar os formulários (POST)
    @PostMapping("/Cadastrar")
    public String cadastrarCliente(/* seus parâmetros */) {
        // Lógica de cadastro
        return "redirect:/Coopase/Cliente?action=Cadastrar";
    }

}
