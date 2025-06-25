package com.example.moinho.Controller.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Coopase")
public class AreaClienteController {

    @GetMapping("/AreaCliente/Servicos")
    public String areaCliente() {
        return "Coopase/AreaCliente/Servicos";
    }

    @GetMapping("/AreaCliente/cadastroView")
    public String direcionarCadastroView(){
        return "Coopase/AreaCliente/CadastroView";
    }

}
