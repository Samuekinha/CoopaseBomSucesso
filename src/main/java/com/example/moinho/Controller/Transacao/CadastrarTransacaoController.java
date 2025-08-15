package com.example.moinho.Controller.Transacao;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/Coopase/Transacao")
public class CadastrarTransacaoController {

//    private final CadastrarContaDepositoService cadastrarContaD;
//
//    // Injeção via construtor
//    public CadastrarContaDepositoController(CadastrarContaDepositoService cadastrarContaD) {
//        this.cadastrarContaD = cadastrarContaD;
//    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarTransacaoView")
    public String cadastrarTransacaoView() {
        return "/Coopase/Transacao/CadastrarTransacaoView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarTransacao (@RequestParam("TipoTransacao") String tipoTransacao,
                           @RequestParam("ValorTransacao") BigDecimal valorTransacao,
                           @RequestParam("ContaDepositoDaTransacao") Long contaDepositoTransacao,
                           @RequestParam("OperadorDaTransacao") Long operadorTransacao,
                           @RequestParam(value = "DescricaoTransacao", required = false)
                                          String descricaoTransacao,
                           RedirectAttributes redirectAttributes) {

        

//        String resposta = cadastrarTransacao.cadastrarTransacao(tipoTransacao, valorTransacao,
//                contaDepositoTransacao, operadorTransacao, descricaoTransacao);

//        redirectAttributes.addFlashAttribute("resposta", resposta);
        return "redirect:/Coopase/Transacao/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Transacao/Servicos";
    }

}
