package com.example.moinho.Controller.Transacao;

import com.example.moinho.Service.Transacao.CadastrarTransacaoService;
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

    private final CadastrarTransacaoService cadastrarTransacaoService;

    // Injeção via construtor
    public CadastrarTransacaoController(CadastrarTransacaoService cadastrarTransacaoService) {
        this.cadastrarTransacaoService = cadastrarTransacaoService;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarTransacaoView")
    public String cadastrarTransacaoView() {
        return "/Coopase/Transacao/CadastrarTransacaoView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarTransacao (@RequestParam("TipoTransacao")
                                            String tipoTransacao,
                           @RequestParam("ValorTransacao")
                                          BigDecimal valorTransacao,
                           @RequestParam("ContaOrigem")
                                          Long contaOrigem,
                           @RequestParam("NomeOperador")
                                          Long operadorTransacao,
                          @RequestParam("FormaTransacao")
                                          String formaTransacao,
                           @RequestParam(value = "ContaDestino", required = false)
                                          Long contaDestino,
                           @RequestParam(value = "DescricaoTransacao", required = false)
                                          String descricaoTransacao,
                           RedirectAttributes redirectAttributes) {

        if (tipoTransacao.equalsIgnoreCase("DEPOSIT")) {

            cadastrarTransacaoService.cadastrarTransacaoEntrada(valorTransacao,
                    contaOrigem, operadorTransacao, formaTransacao, descricaoTransacao);

        } else if (tipoTransacao.equalsIgnoreCase("WITHDRAW")) {

        } else if (tipoTransacao.equalsIgnoreCase("TRANSFER")) {

        } else {
            redirectAttributes.addFlashAttribute("Erro","Impossível" +
                    " completar a operação sem um tipo de transação.");
        }

//        redirectAttributes.addFlashAttribute("resposta", resposta);
        return "redirect:/Coopase/Transacao/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Transacao/Servicos";
    }

}
