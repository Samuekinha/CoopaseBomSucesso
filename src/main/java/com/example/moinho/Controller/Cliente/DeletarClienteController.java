package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import com.example.moinho.Service.S_Cliente.DeletarClienteService;
import com.example.moinho.Service.S_Cliente.EditarClienteService;
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

//        String nomeModel;
//        if (clienteValidadeResposta.startsWith("Erro")){
//            nomeModel = "MensagemErro";
//        } else if(clienteValidadeResposta.startsWith("Sucesso")) {
//            nomeModel = "MensagemSucesso";
//        } else {
//            nomeModel = "MensagemSemMudanca";
//        }
//
//        model.addAttribute(nomeModel, clienteValidadeResposta);

        return "/Coopase/Cliente/Servicos";
    }

    @GetMapping("/Deletar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/Servicos";
    }

}
