package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.S_Cliente.S_CadastroCliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;

@Controller
@RequestMapping("/Coopase/Cliente")
public class CadastrarClienteController {

    private final S_CadastroCliente s_cadastroCliente;

    // Injeção via construtor
    public CadastrarClienteController(S_CadastroCliente s_cadastroCliente) {
        this.s_cadastroCliente = s_cadastroCliente;
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
                           Model model) {

        String[] resultados = s_cadastroCliente.cadastrarCliente(nome, documento, dataNascimento, cooperado,
                dataVencimentoCAF, codigoCaf);

        for (int i = 0; i < resultados.length ; i++) {
            if (resultados[i] != null) {
                model.addAttribute("parametros", resultados[i]);
            }
        }

        return "redirect:/Coopase/Cliente/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Cliente/Servicos";
    }

}
