package com.example.moinho.Controller.Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/Cliente/ServicosCliente";
    }

    @GetMapping("/ConsultarPorPesquisa")
    public String consultarPorPesquisa(
            @RequestParam(value = "pesquisaPorNome", required = false) String pesquisaNome,
            @RequestParam(value = "pesquisaPorDocumento", required = false) String pesquisaPorDocumento,
            @RequestParam(value = "pesquisaPorCodCaf", required = false) String pesquisaPorCodCaf,
            @RequestParam(value = "apenasCooperados", required = false) Boolean apenasCooperados,
            @RequestParam(value = "deZaA", required = false) boolean deZaA,
            Model model) {

        List<E_Cliente> resultados = consultarClientes.consultarComFiltros(
                emptyToNull(pesquisaNome),
                emptyToNull(pesquisaPorDocumento),
                emptyToNull(pesquisaPorCodCaf),
                apenasCooperados,
                deZaA);

        model.addAttribute("resultadoConsulta", resultados);
        return "/Coopase/Cliente/Fragments/TabelaClientes :: corpoTabela";
    }

    private String emptyToNull(String valor) {
        return (valor == null || valor.trim().isEmpty()) ? null : valor.trim();
    }

}
