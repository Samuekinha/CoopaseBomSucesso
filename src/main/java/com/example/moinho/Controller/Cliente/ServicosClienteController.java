package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Controller
@RequestMapping("/Coopase/Cliente/Servicos")
public class ServicosClienteController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public ServicosClienteController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action, Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";

        model.addAttribute("consultaCooperados", consultarClientes.consultarCooperados());
        model.addAttribute("fragmentToLoad", fragment);
        return "Coopase/Cliente/Servicos"; // Sua página principal
    }

}