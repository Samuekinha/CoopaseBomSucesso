package com.example.moinho.Service.Transacao;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.IdInvalidoException;
import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.TransacaoInativaException;
import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.TransacaoNaoEncontradaException;
import com.example.moinho.Entity.Transacao;
import com.example.moinho.Repository.ContaBaseRepository;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InativarTransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaBaseRepository contaBaseRepository;

    public InativarTransacaoService(TransacaoRepository transacaoRepository,
                                    ContaBaseRepository contaBaseRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaBaseRepository = contaBaseRepository;
    }

    @Transactional
    public void inativarTransacao(Long transacaoId) {
        if (transacaoId == null || transacaoId <= 0) {
            throw new IdInvalidoException("O id da transação é inválido");
        }

        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new TransacaoNaoEncontradaException("Transação não encontrada"));

        if (!transacao.getAtiva()) {
            throw new TransacaoInativaException("A transação já está inativa");
        }

        ContaBase contaPrincipal = transacao.getConta_principal();
        ContaBase contaDestino = transacao.getConta_destino();
        BigDecimal valor = transacao.getValor();

        switch (transacao.getTipo_transacao()) {
            case DEPOSITO -> contaPrincipal.aplicarTransacao(valor, Transacao.TypeTransaction.SAQUE);
            case SAQUE -> contaPrincipal.aplicarTransacao(valor, Transacao.TypeTransaction.DEPOSITO);
            case TRANSFERENCIA -> {
                contaPrincipal.aplicarTransacao(valor, Transacao.TypeTransaction.DEPOSITO);
                contaDestino.aplicarTransacao(valor, Transacao.TypeTransaction.SAQUE);
                contaBaseRepository.save(contaDestino);
            }
        }

        contaBaseRepository.save(contaPrincipal);

        transacao.setAtiva(false);
        transacaoRepository.save(transacao);
    }
}
