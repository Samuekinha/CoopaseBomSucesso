package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class ConsultarContaDepositoController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public ConsultarContaDepositoController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarContaDView")
    public String consultarClienteView(Model model) {
        model.addAttribute("resultadoConsulta", consultarClientes.consultarCliente());
        return "/Coopase/Cliente/ConsultarClienteView";
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/ContaDeposito/ServicosContaD";
    }

}
