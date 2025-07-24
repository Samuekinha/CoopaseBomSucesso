package com.example.moinho.Controller.Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Service.S_Cliente.ConsultarClienteService;
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
            @RequestParam(value = "pesquisaPorNascimento", required = false) String pesquisaPorNascimento,
            @RequestParam(value = "pesquisaPorCooperado", required = false) String pesquisaPorCooperado,
            @RequestParam(value = "pesquisaPorVencCaf", required = false) String pesquisaPorVencCaf,
            @RequestParam(value = "pesquisaPorCodCaf", required = false) String pesquisaPorCodCaf,
            @RequestParam(value = "apenasCooperados", required = false) Boolean apenasCooperados,
            @RequestParam(value = "deZ-A", required = false) Boolean deZA,
            Model model) {

        List<E_Cliente> resultados = consultarClientes.consultarClientePorParametro(
                pesquisaNome
                // e os outros filtros se quiser implementar depois
        );

        model.addAttribute("resultadoConsulta", resultados);
        return "/Coopase/Cliente/Fragments/TabelaClientes :: corpoTabela";
    }


//    @GetMapping("/ConsultarPorPesquisa")
//    @ResponseBody
//    public void consultarPorPesquisa(
//            @RequestParam(value = "pesquisaPorNome", required = false) String pesquisaNome,
//            @RequestParam(value = "pesquisaPorDocumento", required = false) String pesquisaPorDocumento,
//            @RequestParam(value = "pesquisaPorNascimento", required = false) String pesquisaPorNascimento,
//            @RequestParam(value = "pesquisaPorCooperado", required = false) String pesquisaPorCooperado,
//            @RequestParam(value = "pesquisaPorVencCaf", required = false) String pesquisaPorVencCaf,
//            @RequestParam(value = "pesquisaPorCodCaf", required = false) String pesquisaPorCodCaf,
//            @RequestParam(value = "apenasCooperados", required = false) Boolean apenasCooperados,
//            @RequestParam(value = "deZ-A", required = false) Boolean deZA,
//            Model model) {
//
//        // Aqui você implementa a lógica de filtro com todos os parâmetros
//        List<E_Cliente> resultados = consultarClientes.consultarClientePorParametro(
//                pesquisaNome
//        );
//
//        model.addAttribute("resultadoConsulta", resultados);
//    }
}
//, pesquisaPorDocumento, pesquisaPorNascimento,
//                pesquisaPorCooperado, pesquisaPorVencCaf, pesquisaPorCodCaf,
//                apenasCooperados, deZA
