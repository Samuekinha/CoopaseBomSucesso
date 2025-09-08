package com.example.moinho.Controller.ContaDeposito;

import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Service.CofreService.ConsultarContaDepositoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Coopase/ContaDeposito")
public class ConsultarContaDepositoController {

    private final ConsultarContaDepositoService consultarContaDeposito;

    // Injeção via construtor
    public ConsultarContaDepositoController(ConsultarContaDepositoService consultarContaDeposito) {
        this.consultarContaDeposito = consultarContaDeposito;
    }

    // Rotas para processar os formulários (POST)
    @GetMapping("/ConsultarContaDepositoView")
    public String consultarContaDepositoView(Model model) {
        model.addAttribute("resultadoConsulta", consultarContaDeposito.consultarTodasContaDeposito());
        return "/Coopase/ContaDeposito/ConsultarContaDepositoView";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> consultarConta(@PathVariable Long id) {
        Optional<E_ContaDeposito> conta = consultarContaDeposito.consultarContaPorId(id);
        if (conta.isEmpty() || conta == null) {
            return ResponseEntity.notFound().build();
        }

        E_ContaDeposito contaDados = conta.get();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", contaDados.getId());
        resposta.put("nomeConta", contaDados.getVaultName());
        resposta.put("saldoAtual", contaDados.getTotal_amount());

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/Consultar")
    public String redirecionamento(Model model) {
        return "/Coopase/ContaDeposito/ServicosContaDeposito";
    }

}
