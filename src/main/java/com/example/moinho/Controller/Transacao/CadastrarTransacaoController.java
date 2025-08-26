package com.example.moinho.Controller.Transacao;

import com.example.moinho.Dto.TransacaoRequest;
import com.example.moinho.Exception.BusinessException;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Service.Transacao.CadastrarTransacaoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String cadastrarTransacao(@Valid TransacaoRequest form,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("Erro", result.getAllErrors());
            return "redirect:/Coopase/Transacao/CadastrarTransacaoView";
        }

        cadastrarTransacaoService.criarTransacaoEntrada(form);

        redirectAttributes.addFlashAttribute("Sucesso", "Transação cadastrada com sucesso!");
        return "redirect:/Coopase/Transacao/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Transacao/Servicos";
    }

}
