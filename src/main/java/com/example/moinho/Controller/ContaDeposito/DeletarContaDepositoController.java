package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.ClienteService.DeletarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class DeletarContaDepositoController {

    private final ConsultarClienteService consultarCliente;
    private final DeletarClienteService deletarCliente;

    // Injeção via construtor
    public DeletarContaDepositoController(ConsultarClienteService consultarClientes, DeletarClienteService deletarCliente) {
        this.consultarCliente = consultarClientes;
        this.deletarCliente = deletarCliente;
    }

    @GetMapping("/DeletarContaDView")
    public String deletarClienteView(Model model) {

        model.addAttribute("resultadoConsulta", consultarCliente.consultarCliente());

        return "/Coopase/ContaDeposito/DeletarContaDView";
    }

    @PostMapping("/Deletar")
    public String deletarCliente(@RequestParam("ClientId") Long id,
                                Model model) {

        String clienteValidadeResposta = deletarCliente.validaDeletarCliente(id);

        return "/Coopase/ContaDeposito/ServicosContaD";
    }

    @GetMapping("/Deletar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/ServicosCliente";
    }

}
