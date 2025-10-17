package com.example.moinho.Service.CofreService;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Repository.ContaBaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EditarContaDepositoService {
    private final ContaBaseRepository r_contaDeposito;

    public EditarContaDepositoService(ContaBaseRepository r_contaDeposito) {
        this.r_contaDeposito = r_contaDeposito;
    }

    private static final int TAMANHO_MAXIMO_NOME = 40;

    public String editarContaDeposito(Long id, String nome) {

        // Validações preliminares antes de buscar no banco
        if (id == null) {
            return "Erro: ID da Conta Deposito não pode ser nulo";
        }

        try {
            // Validação do nome
            if (nome != null && !nome.trim().isEmpty()) {
                String erroNome = validarNome(nome, id);
                if (erroNome != null) return erroNome;
            }

            // Busca a conta existente após as validações básicas
            Optional<ContaBase> contaOptional = r_contaDeposito.findById(id);
            if (contaOptional.isEmpty()) {
                return "Erro: Conta Deposito não encontrada com o ID: " + id;
            }

            ContaBase contaDeposito = contaOptional.get();
            boolean dadosAlterados = false;

            // Aplicar alterações
            if (nome != null && !nome.trim().isEmpty() && !nome.equals(contaDeposito.getNome_conta())) {
                contaDeposito.setNome_conta(nome.trim());
                dadosAlterados = true;
            }

            if (!dadosAlterados) {
                return "Nenhuma alteração foi detectada nos dados da conta de depósito.";
            }

            r_contaDeposito.save(contaDeposito);
            return "Sucesso ao editar a conta de depósito '" + contaDeposito.getNome_conta() + "'!";

        } catch (Exception e) {
            return "Erro ao atualizar conta de depósito: " + e.getMessage();
        }
    }

    private String validarNome(String nome, Long contaId) {
        nome = nome.trim();

        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        Optional<ContaBase> contaExistente = r_contaDeposito.findPorNome(nome);
        if (contaExistente.isPresent() && !contaExistente.get().getId().equals(contaId)) {
            return "Erro: Nome já cadastrado para outra conta";
        }

        return null;
    }
}