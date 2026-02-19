package com.example.moinho.Controller.Diaristas;

import com.example.moinho.Controller.Cliente.ConsultarClienteController;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.Transacao.CadastrarTransacaoService;
import com.example.moinho.Util.FormatadorUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/Diaristas/Servicos")
public class ServicosDiaristasController {

    private final ConsultarClienteService consultarCliente;

    public ServicosDiaristasController(ConsultarClienteService consultarCliente) {
        this.consultarCliente = consultarCliente;
    }

    @GetMapping
    public String servicos(@RequestParam(name = "action", required = false) String action,
                           Model model) {
        // Define o fragmento padrão se action for nulo
        String fragment = (action != null) ? action : "Cadastrar";
        System.out.println("entrou xrc");

        model.addAttribute("formatador", new FormatadorUtil());
        model.addAttribute("fragmentToLoad", fragment);
        return "/Coopase/Diaristas/ServicosDiaristas"; // Sua página principal
    }

}
