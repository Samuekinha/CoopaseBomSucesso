package com.example.moinho.Service.CofreService;

import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Repository.R_ContaDeposito;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CadastrarContaDepositoService {
    private final R_ContaDeposito r_contaDeposito;

    private static final int TAMANHO_MAXIMO_NOME = 40;

    public CadastrarContaDepositoService(R_ContaDeposito r_contaDeposito) {
        this.r_contaDeposito = r_contaDeposito;
    }

    public String cadastrarContaDeposito(String nome) {
        try {
            // Validação do nome
            String erroNome = validarNome(nome);
            if (erroNome != null) {
                return erroNome;
            }

            // Criar e salvar a conta
            E_ContaDeposito conta = new E_ContaDeposito();
            conta.setVault_name(nome.trim());
            conta.setTotal_amount(BigDecimal.valueOf(0.0)); // Saldo inicial zero

            r_contaDeposito.save(conta);

            return "Sucesso: Conta depósito cadastrada com sucesso!";

        } catch (Exception e) {
            return "Erro durante o cadastro: " + e.getMessage();
        }
    }

    private String validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Erro: Nome é obrigatório";
        }

        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        // Verifica se já existe conta com este nome (opcional)
        if (r_contaDeposito.findByvault_name(nome).isPresent()) {
            return "Erro: Já existe uma conta com este nome";
        }

        return null;
    }

}