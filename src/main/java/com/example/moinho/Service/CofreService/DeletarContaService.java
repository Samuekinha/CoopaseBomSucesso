package com.example.moinho.Service.CofreService;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Exception.ContaDepositoExceptions.ErroDesconhecidoException;
import com.example.moinho.Exception.ContaDepositoExceptions.Delete.ContaNaoEncontradaException;
import com.example.moinho.Exception.ContaDepositoExceptions.OperacaoNaoPermitidaException;
import com.example.moinho.Repository.ContaBaseRepository;
import com.example.moinho.Repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeletarContaService {

    private final ContaBaseRepository contaBaseRepo;
    private final TransacaoRepository transacaoRepo;

    public DeletarContaService(ContaBaseRepository contaBaseRepo, TransacaoRepository transacaoRepo) {
        this.contaBaseRepo = contaBaseRepo;
        this.transacaoRepo = transacaoRepo;
    }

    public void DeletarContaDeposito(Long id) {
        try {
            ContaBase contaBase = contaBaseRepo.findById(id)
                    .orElseThrow(() -> new ContaNaoEncontradaException("Conta não foi encontrada."));

            if (!contaBase.isAtiva()) {
                throw new IllegalStateException("Conta já está inativa.");
            }

            List<Long> contasUsadas = transacaoRepo.findAllContasUsadas();

            if (contasUsadas.contains(id)) {
                throw new OperacaoNaoPermitidaException("Essa conta tem transações ligadas e" +
                        " não pode ser desativada.");
            }

            contaBase.setAtiva(false);
            contaBaseRepo.save(contaBase);

        } catch (ContaNaoEncontradaException | OperacaoNaoPermitidaException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroDesconhecidoException("Erro ao desativar conta: " + e.getMessage());
        }
    }
}
