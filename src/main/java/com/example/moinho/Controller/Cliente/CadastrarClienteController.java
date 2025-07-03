package com.example.moinho.Controller.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Coopase/Cliente")
public class CadastrarClienteController {

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarClienteView")
    public String cadastrarCliente(/* seus parâmetros */) {
        // Lógica de cadastro
        return "/Coopase/Cliente/CadastrarClienteView";
    }

}
