package com.example.moinho.Service.CofreService;

import com.example.moinho.Dto.Conta.ContaRequestDTO;
import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Entity.ContaDeposito.ContaFisica;
import com.example.moinho.Entity.ContaDeposito.ContaVirtual;
import com.example.moinho.Exception.ContaDepositoExceptions.ErroDesconhecidoException;
import com.example.moinho.Exception.ContaDepositoExceptions.Cadastro.NomeInvalidoException;
import com.example.moinho.Exception.ContaDepositoExceptions.Cadastro.TipoDeContaInvalidoException;
import com.example.moinho.Repository.ContaBaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class CadastrarContaService {
    private final ContaBaseRepository contaBaseRepository;

    private static final int TAMANHO_MAXIMO_NOME = 40;

    public CadastrarContaService(ContaBaseRepository contaBaseRepository) {
        this.contaBaseRepository = contaBaseRepository;
    }

    public ContaBase cadastrarConta(ContaRequestDTO requestDTO) {
        try {
            // Validação do nome
            String erroNome = validarNome(requestDTO.getNomeConta());
            if (erroNome != null) {
                throw new NomeInvalidoException(erroNome);
            }

            // Criar e salvar a conta
            ContaBase conta = null;
            if (Objects.equals(requestDTO.getTipoConta(), "fisica")) {
                conta = new ContaFisica();
            } else if (Objects.equals(requestDTO.getTipoConta(), "virtual")) {
                conta = new ContaVirtual();
            } else {
                throw new TipoDeContaInvalidoException
                        ("Tipo de conta inválido. Escolha fisíca ou virtual.");
            }
            conta.setNome_conta(requestDTO.getNomeConta().trim());
            conta.setValor_total(BigDecimal.valueOf(0.0)); // Saldo inicial zero


            return contaBaseRepository.save(conta);

        } catch (Exception e) {
            throw new ErroDesconhecidoException("Erro durante o cadastro: " + e.getMessage());
        }
    }

    private String validarNome(String nome) {
        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            throw new NomeInvalidoException("Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME
                    + " caracteres");
        }

        // Verifica se já existe conta com este nome (opcional)
        if (contaBaseRepository.findPorNome(nome).isPresent()) {
            throw new TipoDeContaInvalidoException("Erro: Já existe uma conta com este nome");
        }

        return null;
    }

}