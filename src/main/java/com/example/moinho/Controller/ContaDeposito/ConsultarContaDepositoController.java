package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class ConsultarContaDepositoController {

    private final ConsultarContaDepositoService consultarContaDeposito;

    // Injeção via construtor
    public ConsultarContaDepositoController(ConsultarContaDepositoService consultarContaDeposito) {
        this.consultarContaDeposito = consultarContaDeposito;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarContaDepositoView")
    public String consultarContaDepositoView(Model model) {
        model.addAttribute("resultadoConsulta", consultarContaDeposito.consultarContaDeposito());
        return "/Coopase/ContaDeposito/ConsultarContaDepositoView";
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/ContaDeposito/ServicosContaDeposito";
    }

}
