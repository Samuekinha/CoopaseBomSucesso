package com.example.moinho.Controller.Transacao;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Service.Transacao.ConsultarTransacaoService;
import com.example.moinho.Service.Transacao.InativarTransacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Coopase/Transacao")
public class InativarTransacaoController {

    private final ConsultarTransacaoService consultarTransacaoService;
    private final InativarTransacaoService inativarTransacaoService;

    // Injeção via construtor
    public InativarTransacaoController(ConsultarTransacaoService consultarTransacaoService, InativarTransacaoService inativarTransacaoService) {
        this.consultarTransacaoService = consultarTransacaoService;
        this.inativarTransacaoService = inativarTransacaoService;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/InativarTransacaoView")
    public String consultarTransacaoView(Model model) {

        List<TransacaoResumoDTO> listaTransacoes = consultarTransacaoService.consultarTodasTransacao();

        model.addAttribute("ListaTransacoes", listaTransacoes);

        return "/Coopase/Transacao/InativarTransacaoView";
    }

    @PostMapping("/Inativar")
    public String inativarTransacao(@RequestParam("idTransacao") Long transacaoId,
                                Model model) {

        if (transacaoId != null) {
            inativarTransacaoService.inativarTransacao(transacaoId);
        }

        return "/Coopase/Transacao/ServicosTransacao";
    }

    @GetMapping("/Inativar")
    public String redirecionamento(Model model) {
        return "/Coopase/Transacao/ServicosTransacao";
    }

}
