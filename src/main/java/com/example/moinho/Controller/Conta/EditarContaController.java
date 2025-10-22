package com.example.moinho.Controller.Conta;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.CofreService.EditarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Coopase/Conta")
public class EditarContaController {

    private final ConsultarContaDepositoService consultarContaDeposito;
    private final EditarContaDepositoService contaDeposito;

    // Injeção via construtor
    public EditarContaController(ConsultarContaDepositoService consultarContaDeposito, EditarContaDepositoService contaDeposito) {
        this.consultarContaDeposito = consultarContaDeposito;
        this.contaDeposito = contaDeposito;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/EditarContaView")
    public String editarContaView(Model model) {

        List<ContaBase> resultadoConsulta = consultarContaDeposito.consultarTodasContaDeposito();

        model.addAttribute("resultadoConsulta", resultadoConsulta);

        return "/Coopase/Conta/EditarContaView";
    }

    @PostMapping("/Editar")
    public String editarConta(@RequestParam("ContaDepositoId") Long id,
                                @RequestParam(value = "ContaDepositoNome", required = false) String nome,
                                RedirectAttributes redirectAttributes){

        String resposta = contaDeposito.editarContaDeposito(id, nome);

        redirectAttributes.addFlashAttribute("resposta", resposta);

        return "redirect:/Coopase/Conta/Servicos";
    }

    @GetMapping("/Editar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Conta/Servicos";
    }

}
