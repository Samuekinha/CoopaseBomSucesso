package com.example.moinho.Controller.Transacao;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.Transacao.ConsultarTransacaoService;
import com.example.moinho.Util.FormatadorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/Transacao/Servicos")
public class ServicosTransacaoController {

    private final ConsultarContaDepositoService contaDepositoService;
    private final ConsultarClienteService consultarClienteService;
    private final ConsultarTransacaoService consultarTransacaoService;

    // Injeção via construtor
    public ServicosTransacaoController(ConsultarContaDepositoService contaDepositoService,
                                       ConsultarClienteService consultarClienteService, ConsultarTransacaoService consultarTransacaoService) {
        this.contaDepositoService = contaDepositoService;
        this.consultarClienteService = consultarClienteService;
        this.consultarTransacaoService = consultarTransacaoService;
    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action,
                           Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";

        model.addAttribute("formatador", new FormatadorUtil());
        model.addAttribute("fragmentToLoad", fragment);
        return "Coopase/Transacao/ServicosTransacao"; // Sua página principal
    }
}