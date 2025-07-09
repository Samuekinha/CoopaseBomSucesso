package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Coopase/Cliente")
public class EditarClienteController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public EditarClienteController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/EditarClienteView")
    public String editarClienteView(Model model) {

        model.addAttribute("resultadoConsulta", consultarClientes.consultarCliente());

        return "/Coopase/Cliente/EditarClienteView";
    }

    @PostMapping("/Editar")
    public String editarCliente() {



        return null;
    }

}
