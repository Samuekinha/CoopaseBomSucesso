package com.example.moinho.Controller.Conta;

import com.example.moinho.Dto.Conta.ContaRequestDTO;
import com.example.moinho.Service.CofreService.CadastrarContaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Coopase/Conta")
public class CadastrarContaController {

    private final CadastrarContaService cadastrarContaService;

    // Injeção via construtor
    public CadastrarContaController(CadastrarContaService cadastrarContaService) {
        this.cadastrarContaService = cadastrarContaService;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarContaView")
    public String viewCadastroConta() {
        return "Coopase/Conta/CadastrarContaView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarConta(ContaRequestDTO requestDTO,
                           RedirectAttributes redirectAttributes) {

        cadastrarContaService.cadastrarConta(requestDTO);

        redirectAttributes.addFlashAttribute("Sucesso", "Conta cadastrada com sucesso!");
        return "Coopase/Conta/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Conta/Servicos";
    }

}
