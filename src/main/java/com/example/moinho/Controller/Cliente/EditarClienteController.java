package com.example.moinho.Controller.Cliente;

import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.ClienteService.EditarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/Coopase/Cliente")
public class EditarClienteController {

    private final ConsultarClienteService consultarCliente;
    private final EditarClienteService editarCliente;

    // Injeção via construtor
    public EditarClienteController(ConsultarClienteService consultarClientes, EditarClienteService editarCliente) {
        this.consultarCliente = consultarClientes;
        this.editarCliente = editarCliente;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/EditarClienteView")
    public String editarClienteView(Model model) {

        model.addAttribute("resultadoConsulta", consultarCliente.consultarCliente());

        return "/Coopase/Cliente/EditarClienteView";
    }

    @PostMapping("/Editar")
    public String editarCliente(@RequestParam("ClientId") Long id,
                                @RequestParam(value = "ClientName", required = false) String nome,
                                @RequestParam(value = "ClientDocument", required = false) String documento,
                                @RequestParam(value = "ClientBirth", required = false) LocalDate dataNascimento,
                                @RequestParam(value = "cooperadoSelect", required = false) boolean cooperado,
                                @RequestParam(value = "ClientCafDate", required = false) LocalDate vencimentoCaf,
                                @RequestParam(value = "ClientCafCode", required = false) String codigoCaf,
                                RedirectAttributes redirectAttributes){

        String resposta = editarCliente.editarCliente(id, nome, documento,
                dataNascimento, cooperado, vencimentoCaf, codigoCaf);

        redirectAttributes.addFlashAttribute("resposta", resposta);


        return "redirect:/Coopase/Cliente/Servicos";
    }

    @GetMapping("/Editar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/ServicosCliente";
    }

}
