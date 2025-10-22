package com.example.moinho.Controller.Conta;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Coopase/Conta")
public class ConsultarContaController {

    private final ConsultarContaDepositoService consultarContaDeposito;

    // Injeção via construtor
    public ConsultarContaController(ConsultarContaDepositoService consultarContaDeposito) {
        this.consultarContaDeposito = consultarContaDeposito;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarContaView")
    public String consultarContaView(Model model) {
        model.addAttribute("resultadoConsulta", consultarContaDeposito.consultarTodasContaDeposito());
        return "/Coopase/Conta/ConsultarContaView";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> consultarConta(@PathVariable Long id) {
        Optional<ContaBase> conta = consultarContaDeposito.consultarContaPorId(id);
        if (conta.isEmpty() || conta == null) {
            return ResponseEntity.notFound().build();
        }

        ContaBase contaDados = conta.get();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", contaDados.getId());
        resposta.put("nomeConta", contaDados.getNome_conta());
        resposta.put("saldoAtual", contaDados.getValor_total());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/Conta/ServicosConta";
    }

}
