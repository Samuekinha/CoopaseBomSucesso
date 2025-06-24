package com.example.moinho.Controller.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Cliente")
public class ClienteCadastroController {

    @GetMapping("/Cadastro")
    public String cadastraCliente(){



        return "/Cadastro";
    }

}
