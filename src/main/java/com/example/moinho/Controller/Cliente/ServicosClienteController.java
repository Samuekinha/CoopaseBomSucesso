package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Util.FormatadorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Coopase/Cliente/Servicos")
public class ServicosClienteController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public ServicosClienteController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action,
                           @RequestParam(value = "toggleCooperados", required = false) Boolean botaoListaCompleta,
                           Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";

        if (Boolean.TRUE.equals(botaoListaCompleta)) {
            model.addAttribute("listaCooperados", consultarClientes.consultarTodosCooperados());
        } else {
            model.addAttribute("listaCooperados", consultarClientes.consultar10Cooperados());
        }

        int[] infosProcessadasCooperados = consultarClientes.consultarInfosProcessadasCooperados();
        model.addAttribute("qtdeDeCooperados",
                infosProcessadasCooperados[0]);
        model.addAttribute("qtdeDeDapsAtivas",
                infosProcessadasCooperados[1]);

        model.addAttribute("listaVendedores", consultarClientes.consultarVendedores());
        model.addAttribute("quantidadeClientes", consultarClientes.consultarQuantidadeClientes());
        model.addAttribute("formatador", new FormatadorUtil());
        model.addAttribute("fragmentToLoad", fragment);
        return "Coopase/Cliente/ServicosCliente"; // Sua página principal
    }

}