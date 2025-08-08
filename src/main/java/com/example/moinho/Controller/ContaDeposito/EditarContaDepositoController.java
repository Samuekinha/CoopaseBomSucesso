package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.ClienteService.EditarClienteService;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import com.example.moinho.Service.CofreService.EditarContaDepositoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class EditarContaDepositoController {

    private final ConsultarContaDepositoService consultarContaDeposito;
    private final EditarContaDepositoService contaDeposito;

    // Injeção via construtor
    public EditarContaDepositoController(ConsultarContaDepositoService consultarContaDeposito, EditarContaDepositoService contaDeposito) {
        this.consultarContaDeposito = consultarContaDeposito;
        this.contaDeposito = contaDeposito;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/EditarContaDepositoView")
    public String editarContaDepositoView(Model model) {

        List<E_ContaDeposito> resultadoConsulta = consultarContaDeposito.consultarContaDeposito();

        model.addAttribute("resultadoConsulta", resultadoConsulta);

        return "/Coopase/ContaDeposito/EditarContaDepositoView";
    }

    @PostMapping("/Editar")
    public String editarContaDeposito(@RequestParam("ContaDepositoId") Long id,
                                @RequestParam(value = "ContaDepositoNome", required = false) String nome,
                                RedirectAttributes redirectAttributes){

        String resposta = contaDeposito.editarContaDeposito(id, nome);

        redirectAttributes.addFlashAttribute("resposta", resposta);
        
        return "redirect:/Coopase/ContaDeposito/Servicos";
    }

    @GetMapping("/Editar")
    public String redirecionamento(Model model) {
        return "/Coopase/ContaDeposito/ServicosContaDeposito";
    }

}
