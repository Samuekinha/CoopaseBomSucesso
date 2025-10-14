package com.example.moinho.Service.Transacao;

import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.TransacaoAtivaException;
import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.IdInvalidoException;
import com.example.moinho.Exception.TransacaoExceptions.InativarEReativarExceptions.TransacaoNaoEncontradaException;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReativarTransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaDepositoRepository contaDepositoRepository;

    public ReativarTransacaoService(TransacaoRepository transacaoRepository,
                                    ContaDepositoRepository contaDepositoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaDepositoRepository = contaDepositoRepository;
    }

    @Transactional
    public void reativarTransacao(Long transacaoId) {
        if (transacaoId == null || transacaoId <= 0) {
            throw new IdInvalidoException("O id da transação é inválido");
        }

        TransacaoTable transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new TransacaoNaoEncontradaException("Transação não encontrada"));

        if (transacao.getAtiva()) {
            throw new TransacaoAtivaException("A transação já está ativa");
        }

        E_ContaDeposito contaPrincipal = transacao.getContaDeposito();
        E_ContaDeposito contaDestino = transacao.getContaDestino();
        BigDecimal valor = transacao.getValue();

        switch (transacao.getTypeTransaction()) {
            case DEPOSIT -> contaPrincipal.aplicarTransacao(valor, TransacaoTable.TypeTransaction.DEPOSIT);
            case WITHDRAW -> contaPrincipal.aplicarTransacao(valor, TransacaoTable.TypeTransaction.WITHDRAW);
            case TRANSFER -> {
                contaPrincipal.aplicarTransacao(valor, TransacaoTable.TypeTransaction.WITHDRAW);
                contaDestino.aplicarTransacao(valor, TransacaoTable.TypeTransaction.DEPOSIT);
                contaDepositoRepository.save(contaDestino);
            }
        }

        contaDepositoRepository.save(contaPrincipal);

        transacao.setAtiva(true);
        transacaoRepository.save(transacao);
    }
}
