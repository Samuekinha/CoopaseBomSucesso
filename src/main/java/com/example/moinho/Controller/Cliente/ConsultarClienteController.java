package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Coopase/Cliente")
public class ConsultarClienteController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public ConsultarClienteController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarClienteView")
    public String consultarClienteView(Model model) {
        model.addAttribute("resultadoConsulta", consultarClientes.consultarCliente());
        return "/Coopase/Cliente/ConsultarClienteView";
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/ServicosCliente";
    }

    @PostMapping("/ConsultarPesquisa")
    public String consultarPorPesquisa(
            @RequestParam(value = "pesquisaPorNome", required = false) String pesquisaNome,
            Model model) {

        if (pesquisaNome != null && !pesquisaNome.trim().isEmpty()) {
            model.addAttribute("resultadoConsulta",
                    consultarClientes.consultarClientePorParametro(pesquisaNome));
        } else {
            model.addAttribute("resultadoConsulta", consultarClientes.consultarCliente());
        }

        return "/Coopase/Cliente/ConsultarClienteView :: #listaDeClientesSelecinavel";
    }

}
