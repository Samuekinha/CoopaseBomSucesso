package com.example.moinho.Controller.Cliente;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Controller
@RequestMapping("/Coopase/Cliente/Servicos")
public class ServicosClienteController {

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action, Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";
        model.addAttribute("fragmentToLoad", fragment);
        return "Coopase/Cliente/Servicos"; // Sua página principal
    }

    @GetMapping("/{action}")
    @ResponseBody
    public String getFragment(@PathVariable String action, Model model) {
        // Garante que o retorno é APENAS o fragmento HTML
        return templateEngine.process(
                "Coopase/Cliente/" + action + "ClienteView :: " + action,
                new Context(Locale.getDefault(), model.asMap())
        );
    }


}