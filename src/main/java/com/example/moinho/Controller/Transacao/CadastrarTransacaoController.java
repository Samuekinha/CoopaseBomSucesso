package com.example.moinho.Controller.Transacao;

import com.example.moinho.Dto.TransacaoRequest;
import com.example.moinho.Exception.BusinessException;
import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.Transacao.CadastrarTransacaoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Coopase/Transacao")
public class CadastrarTransacaoController {

    private final CadastrarTransacaoService cadastrarTransacaoService;
    private final ConsultarContaDepositoService contaDepositoService;
    private final ConsultarClienteService consultarClienteService;

    // Injeção via construtor
    public CadastrarTransacaoController(CadastrarTransacaoService cadastrarTransacaoService, ConsultarContaDepositoService contaDepositoService, ConsultarClienteService consultarClienteService) {
        this.cadastrarTransacaoService = cadastrarTransacaoService;
        this.contaDepositoService = contaDepositoService;
        this.consultarClienteService = consultarClienteService;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarTransacaoView")
    public String cadastrarTransacaoView(Model model) {
        try {
            List<E_ContaDeposito> contasDeposito = contaDepositoService.consultarTodasContaDeposito();
            model.addAttribute("contasDeposito", contasDeposito);
        } catch (Exception e) {
            System.err.println("Erro ao carregar contas de depósito: " + e.getMessage());
            model.addAttribute("contasDeposito", List.of()); // Lista vazia em caso de erro
        }

        // Buscar todos os vendedores/operadores para o select
        try {
            List<E_Cliente> vendedores = consultarClienteService.consultarVendedores();
            model.addAttribute("vendedores", vendedores);
        } catch (Exception e) {
            System.err.println("Erro ao carregar vendedores: " + e.getMessage());
            model.addAttribute("vendedores", List.of()); // Lista vazia em caso de erro
        }

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
