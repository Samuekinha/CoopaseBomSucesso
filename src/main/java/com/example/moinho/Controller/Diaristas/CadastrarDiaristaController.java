package com.example.moinho.Controller.Diaristas;

import com.example.moinho.Dto.Diaristas.DiaristaRequestDTO;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Service.ClienteService.ConsultarClienteService;
import com.example.moinho.Service.Diaristas.CadastrarDiarista;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/Coopase/Diaristas")
public class CadastrarDiaristaController {

    private final ConsultarClienteService consultarCliente;
    private final CadastrarDiarista cadastrarDiarista;

    public CadastrarDiaristaController(ConsultarClienteService consultarCliente,
                           CadastrarDiarista cadastrarDiarista) {
        this.consultarCliente = consultarCliente;
        this.cadastrarDiarista = cadastrarDiarista;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/CadastrarDiaristaView")
    public String cadastrarDiaristasView(Model model) {
        model.addAttribute("listaCadastro",
                consultarCliente.ConsultarClienteNaoDiarista());
        return "Coopase/Diaristas/CadastrarDiaristaView";
    }

    @PostMapping("/Cadastrar")
    public String cadastrarDiaristas(@Valid DiaristaRequestDTO form,
                                     BindingResult result,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("Erro", result.getAllErrors());
            return "redirect:/Coopase/Diaristas/Servicos";
        }

        cadastrarDiarista.cadastrarDiarista(form);

        redirectAttributes.addFlashAttribute("Sucesso", "Transação cadastrada com sucesso!");
        return "redirect:/Coopase/Diaristas/Servicos";
    }

    @GetMapping("/Cadastrar")
    public String redirecionamento(Model model) {
        return "redirect:/Coopase/Diaristas/Servicos";
    }

}
