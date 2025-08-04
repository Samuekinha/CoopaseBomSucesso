package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.ClienteService.CadastrarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/Coopase/Cliente")
public class CadastrarClienteController {

    private final CadastrarClienteService cadastrarClienteService;

    // Injeção via construtor
    public CadastrarClienteController(CadastrarClienteService cadastrarClienteService) {
        this.cadastrarClienteService = cadastrarClienteService;
    }

//    private static final String[] parametros = {"nome", "documento", "dataNascimento", "cooperado",
//    "dataVencimentoCaf", "codigoCaf", "status"};

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarClienteView")
    public String viewCadastroCliente() {
        return "/Coopase/Cliente/CadastrarClienteView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarCliente(@RequestParam(value = "ClientName") String nome,
                           @RequestParam(value = "ClientDocument", required = false) String documento,
                           @RequestParam(value = "ClientBirth", required = false) LocalDate dataNascimento,
                           @RequestParam(value = "cooperadoSelect" , required = false) boolean cooperado,
                           @RequestParam(value = "ClientCafDate", required = false) LocalDate dataVencimentoCAF,
                           @RequestParam(value = "ClientCafCode", required = false) String codigoCaf,
                           RedirectAttributes redirectAttributes) {

        String resposta = cadastrarClienteService.cadastrarCliente(nome, documento, dataNascimento, cooperado,
                dataVencimentoCAF, codigoCaf);

        redirectAttributes.addFlashAttribute("resposta", resposta);

        return "redirect:/Coopase/Cliente/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Cliente/Servicos";
    }

}
