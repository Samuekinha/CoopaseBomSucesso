package com.example.moinho.Service.Transacao;

import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.ContaDestinoNaoEncontradaException;
import com.example.moinho.Exception.TransacaoExceptions.InativarExceptions.IdInvalidoException;
import com.example.moinho.Exception.TransacaoExceptions.InativarExceptions.TransacaoInativaException;
import com.example.moinho.Exception.TransacaoExceptions.InativarExceptions.TransacaoNaoEncontradaException;
import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.Response.OperationResult;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.R_Cliente;
import com.example.moinho.Repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InativarTransacaoService {

    private final TransacaoRepository transacaoRepository;

    public InativarTransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    public void inativarTransacao(Long transacaoId) {

        if (transacaoId <= 0) {
            throw new IdInvalidoException("O id da transação é inválido");
        }

        TransacaoTable transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new TransacaoNaoEncontradaException
                        ("Transação não encontrada"));

        if (!transacao.getAtiva()) {
            throw new TransacaoInativaException("A transação já está inativa");
        }

        transacao.setAtiva(false);
        transacaoRepository.save(transacao);
    }

}
