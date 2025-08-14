package com.example.moinho.Controller.Transacao;

import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Coopase/Transacao")
public class ConsultarTransacaoController {

    private final ConsultarContaDepositoService consultarContaDeposito;

    // Injeção via construtor
    public ConsultarTransacaoController(ConsultarContaDepositoService consultarContaDeposito) {
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
