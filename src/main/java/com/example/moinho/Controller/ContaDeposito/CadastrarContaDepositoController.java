package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Service.ClienteService.CadastrarClienteService;
import com.example.moinho.Service.CofreService.CadastrarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class CadastrarContaDepositoController {

    private final CadastrarContaDepositoService cadastrarContaD;

    // Injeção via construtor
    public CadastrarContaDepositoController(CadastrarContaDepositoService cadastrarContaD) {
        this.cadastrarContaD = cadastrarContaD;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarContaDView")
    public String viewCadastroCliente() {
        return "/Coopase/ContaDeposito/CadastrarContaDView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarContaD(@RequestParam(value = "ContaDNome") String nome,
                           RedirectAttributes redirectAttributes) {

        String resposta = cadastrarContaD.cadastrarContaDeposito(nome);

        redirectAttributes.addFlashAttribute("resposta", resposta);

        return "redirect:/Coopase/ContaDeposito/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/ContaDeposito/Servicos";
    }

}
