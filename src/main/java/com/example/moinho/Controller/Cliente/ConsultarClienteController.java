package com.example.moinho.Controller.Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import com.example.moinho.Service.S_Cliente.S_CadastroCliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/Coopase/Cliente")
public class ConsultarClienteController {

    private final ConsultarClienteService consultarClientes;

    // Injeção via construtor
    public ConsultarClienteController(ConsultarClienteService consultarClientes) {
        this.consultarClientes = consultarClientes;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarClienteView")
    public String consultarClienteView(Model model) {

        model.addAttribute("resultadoConsulta", consultarClientes.consultarCliente());

        return "/Coopase/Cliente/ConsultarClienteView";
    }

}
