package com.example.moinho.Controller.Conta;

import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Util.FormatadorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/Conta/Servicos")
public class ServicosContaController {

    private final ConsultarContaDepositoService consultarContaD;

    // Injeção via construtor
    public ServicosContaController(ConsultarContaDepositoService consultarContaD) {
        this.consultarContaD = consultarContaD;
    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action,
                           Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";

        model.addAttribute("valorTotalContas",
                consultarContaD.ConsultarValorTotalContas());
        model.addAttribute("formatador", new FormatadorUtil());
        model.addAttribute("fragmentToLoad", fragment);
        return "/Coopase/Conta/ServicosConta"; // Sua página principal
    }

}
