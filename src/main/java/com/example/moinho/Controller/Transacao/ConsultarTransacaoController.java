package com.example.moinho.Controller.Transacao;

import com.example.moinho.Service.Transacao.ConsultarTransacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Coopase/Transacao")
public class ConsultarTransacaoController {

    private final ConsultarTransacaoService consultarTransacaoService;

    // Injeção via construtor
    public ConsultarTransacaoController(ConsultarTransacaoService consultarTransacaoService) {
        this.consultarTransacaoService = consultarTransacaoService;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarTransacaoView")
    public String consultarTransacaoView(Model model) {

        return "/Coopase/Transacao/ConsultarContaDepositoView";
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/Transacao/ServicosTransacao";
    }

}
