package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.ClienteService.DeletarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class DeletarContaDepositoController {

    private final ConsultarContaDepositoService consultarContaDeposito;
    private final DeletarClienteService deletarCliente;

    // Injeção via construtor
    public DeletarContaDepositoController(ConsultarContaDepositoService consultarContaDeposito,
                                          DeletarClienteService deletarCliente) {
        this.consultarContaDeposito = consultarContaDeposito;
        this.deletarCliente = deletarCliente;
    }

    @GetMapping("/DeletarContaDepositoView")
    public String deletarContaDepositoView(Model model) {

        model.addAttribute("resultadoConsulta", consultarContaDeposito.consultarContaDeposito());

        return "/Coopase/ContaDeposito/DeletarContaDepositoView";
    }

    @PostMapping("/Deletar")
    public String deletarContaDeposito(@RequestParam("ContaDepositoId") Long id,
                                Model model) {

        String contaDepositoValidadeResposta = deletarCliente.validaDeletarCliente(id);

        return "/Coopase/ContaDeposito/ServicosContaDeposito";
    }

    @GetMapping("/Deletar")
    public String redirecionamento(Model model) {
        return "/Coopase/ContaDeposito/ServicosContaDeposito";
    }

}
