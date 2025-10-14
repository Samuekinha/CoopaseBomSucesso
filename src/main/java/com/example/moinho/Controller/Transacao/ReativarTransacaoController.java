package com.example.moinho.Controller.Transacao;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Service.Transacao.ConsultarTransacaoService;
import com.example.moinho.Service.Transacao.InativarTransacaoService;
import com.example.moinho.Service.Transacao.ReativarTransacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/Coopase/Transacao")
public class ReativarTransacaoController {

    private final ReativarTransacaoService reativarTransacaoService;

    // Injeção via construtor
    public ReativarTransacaoController(ReativarTransacaoService reativarTransacaoService) {
        this.reativarTransacaoService = reativarTransacaoService;
    }

    @PostMapping("/Reativar")
    public String inativarTransacao(@RequestParam("idTransacao") Long transacaoId,
                                Model model) {

        if (transacaoId != null) {
            reativarTransacaoService.reativarTransacao(transacaoId);
        }

        return "/Coopase/Transacao/ServicosTransacao";
    }

    @GetMapping("/Reativar")
    public String redirecionamento(Model model) {
        return "/Coopase/Transacao/ServicosTransacao";
    }

}
