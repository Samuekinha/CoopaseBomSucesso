package com.example.moinho.Controller.Conta;

import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.CofreService.DeletarContaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/Coopase/Conta")
public class DeletarContaController {

    private final ConsultarContaDepositoService consultarContaDeposito;
    private final DeletarContaService deletarContaService;

    // Injeção via construtor
    public DeletarContaController(ConsultarContaDepositoService consultarContaDeposito, DeletarContaService deletarContaService) {
        this.consultarContaDeposito = consultarContaDeposito;
        this.deletarContaService = deletarContaService;
    }

    @GetMapping("/DeletarContaView")
    public String deletarContaView(Model model) {

        model.addAttribute("resultadoConsulta",
                consultarContaDeposito.consultarTodasContaDeposito());

        return "/Coopase/Conta/DeletarContaView";
    }

    @PostMapping("/Deletar")
    public String deletarConta(@RequestParam("ContaDepositoId") Long id,
                               RedirectAttributes redirectAttributes) {
        deletarContaService.DeletarContaDeposito(id);

        redirectAttributes.addFlashAttribute("Sucesso", "Conta deletada com sucesso!");
        return "redirect:/Coopase/Conta/Servicos";
    }

    @GetMapping("/Deletar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Conta/Servicos";
    }

}
