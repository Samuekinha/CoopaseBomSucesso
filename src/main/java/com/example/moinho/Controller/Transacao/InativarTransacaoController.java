package com.example.moinho.Controller.Transacao;

import com.example.moinho.Model.Response.OperationResult;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.CofreService.DeletarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/Transacao")
public class InativarTransacaoController {

    private final ConsultarContaDepositoService consultarContaDeposito;
    private final DeletarContaDepositoService deletarContaDepositoService;

    // Injeção via construtor
    public InativarTransacaoController(ConsultarContaDepositoService consultarContaDeposito, DeletarContaDepositoService deletarContaDepositoService) {
        this.consultarContaDeposito = consultarContaDeposito;
        this.deletarContaDepositoService = deletarContaDepositoService;
    }

    @GetMapping("/InativarTransacaoView")
    public String deletarContaDepositoView(Model model) {

        model.addAttribute("resultadoConsulta",
                consultarContaDeposito.consultarContaDeposito());

        return "/Coopase/Transacao/InativarTransacaoView";
    }

    @PostMapping("/Inativar")
    public String inativarTransacao(@RequestParam("IdTransacao") Long id,
                                Model model) {

        OperationResult contaDepositoValidadeResposta =
                deletarContaDepositoService.DeletarContaDeposito(id);

        return "/Coopase/Transacao/ServicosTransacao";
    }

    @GetMapping("/inativar")
    public String redirecionamento(Model model) {
        return "/Coopase/Transacao/ServicosTransacao";
    }

}
