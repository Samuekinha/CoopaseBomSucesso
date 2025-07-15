package com.example.moinho.Controller.Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
import com.example.moinho.Service.S_Cliente.EditarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
                                @RequestParam("ClientName") String nome,
                                @RequestParam("ClientDocument") String documento,
                                @RequestParam("ClientBirth") LocalDate dataNascimento,
                                @RequestParam("cooperadoSelect") boolean cooperado,
                                @RequestParam("ClientCafDate") LocalDate vencimentoCaf,
                                @RequestParam("ClientCafCode") String codigoCaf,
                                Model model){

        String clienteValidadeResposta = editarCliente.validaEditarCliente(id, nome, documento,
                dataNascimento, cooperado, vencimentoCaf, codigoCaf);

        if (clienteValidadeResposta.startsWith("Erro")){
            model.addAttribute("MensagemErro", clienteValidadeResposta);
        } else if(clienteValidadeResposta.startsWith("Sucesso")) {
            model.addAttribute("MensagemSucesso", clienteValidadeResposta);
        } else {
            model.addAttribute("MensagemSemMudanca", clienteValidadeResposta);
        }

        return "redirect:/Coopase/Cliente/Servicos";
    }

}
