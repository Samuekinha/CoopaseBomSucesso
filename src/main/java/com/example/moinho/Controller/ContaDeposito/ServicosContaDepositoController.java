package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Util.FormatadorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/ContaDeposito/Servicos")
public class ServicosContaDepositoController {

//    private final ConsultarClienteService consultarClientes;
//
//    // Injeção via construtor
//    public ServicosClienteController(ConsultarClienteService consultarClientes) {
//        this.consultarClientes = consultarClientes;
//    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action,
                           Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";


        model.addAttribute("formatador", new FormatadorUtil());
        model.addAttribute("fragmentToLoad", fragment);
        return "Coopase/ContaDeposito/ServicosContaD"; // Sua página principal
    }

}
