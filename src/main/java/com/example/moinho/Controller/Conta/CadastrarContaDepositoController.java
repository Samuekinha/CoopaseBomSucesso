package com.example.moinho.Controller.Conta;

import com.example.moinho.Dto.Conta.ContaRequestDTO;
import com.example.moinho.Service.CofreService.CadastrarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class CadastrarContaDepositoController {

    private final CadastrarContaDepositoService cadastrarContaD;

    // Injeção via construtor
    public CadastrarContaDepositoController(CadastrarContaDepositoService cadastrarContaD) {
        this.cadastrarContaD = cadastrarContaD;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarContaDepositoView")
    public String viewCadastroContaDeposito() {
        return "/Coopase/ContaDeposito/CadastrarContaDepositoView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarContaDeposito(ContaRequestDTO requestDTO,
                           RedirectAttributes redirectAttributes) {

        String resposta = cadastrarContaD.cadastrarContaDeposito(requestDTO);

        redirectAttributes.addFlashAttribute("resposta", resposta);

        return "redirect:/Coopase/ContaDeposito/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/ContaDeposito/Servicos";
    }

}
