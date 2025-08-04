package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.ClienteService.DeletarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/Coopase/Cliente")
public class DeletarClienteController {

    private final ConsultarClienteService consultarCliente;
    private final DeletarClienteService deletarCliente;

    // Injeção via construtor
    public DeletarClienteController(ConsultarClienteService consultarClientes, DeletarClienteService deletarCliente) {
        this.consultarCliente = consultarClientes;
        this.deletarCliente = deletarCliente;
    }

    @GetMapping("/DeletarClienteView")
    public String deletarClienteView(Model model) {

        model.addAttribute("resultadoConsulta", consultarCliente.consultarCliente());

        return "/Coopase/Cliente/DeletarClienteView";
    }

    @PostMapping("/Deletar")
    public String deletarCliente(@RequestParam("ClientId") Long id,
                                Model model) {

        String clienteValidadeResposta = deletarCliente.validaDeletarCliente(id);

        return "/Coopase/Cliente/ServicosCliente";
    }

    @GetMapping("/Deletar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/ServicosCliente";
    }

}
