package com.example.moinho.Service.Transacao;

import com.example.moinho.Dto.Transacao.TransacaoRequestDTO;
import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.*;
import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Repository.R_Cliente;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CadastrarTransacaoService {

    private final ContaDepositoRepository contaDepositoRepository;
    private final TransacaoRepository transacaoRepository;
    private final R_Cliente clienteRepository;

    public CadastrarTransacaoService(ContaDepositoRepository contaDepositoRepository,
                                     TransacaoRepository transacaoRepository,
                                     R_Cliente clienteRepository) {
        this.contaDepositoRepository = contaDepositoRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public TransacaoTable criarTransacao(TransacaoRequestDTO request, boolean auto) {

        E_ContaDeposito contaPrincipal = contaDepositoRepository.findById(request.getContaPrincipalId())
                .orElseThrow(() -> new ContaPrincipalNaoEncontradaException("Conta principal não encontrada"));

        if (!contaPrincipal.isActive()) {
            throw new ContaPrincipalInativaException("Conta principal está inativa");
        }

        E_ContaDeposito contaDestino = null;
        if (request.getTipoTransacao().equals(TransacaoTable.TypeTransaction.TRANSFER)) {
            contaDestino = contaDepositoRepository.findById(request.getContaDestinoId())
                    .orElseThrow(() -> new ContaDestinoNaoEncontradaException("Conta destino não encontrada"));

            if (!contaDestino.isActive()) {
                throw new ContaDestinoInativaException("Conta destino está inativa");
            }
        }

        E_Cliente operador = clienteRepository.findById(request.getOperadorTransacaoId())
                .orElseThrow(() -> new OperadorNaoEncontradoException("Operador não encontrado"));

        if (!operador.isOperator()) {
            throw new OperadorSemPermissaoException("Operador não tem permissão");
        }

        BigDecimal saldoAnterior = contaPrincipal.getTotal_amount();

        switch (request.getTipoTransacao()) {
            case DEPOSIT -> contaPrincipal.aplicarTransacao(request.getValorTransacao(), TransacaoTable.TypeTransaction.DEPOSIT);
            case WITHDRAW -> contaPrincipal.aplicarTransacao(request.getValorTransacao(), TransacaoTable.TypeTransaction.WITHDRAW);
            case TRANSFER -> {
                contaPrincipal.aplicarTransacao(request.getValorTransacao(), TransacaoTable.TypeTransaction.WITHDRAW);
                contaDestino.aplicarTransacao(request.getValorTransacao(), TransacaoTable.TypeTransaction.DEPOSIT);
                contaDepositoRepository.save(contaDestino);
            }
        }

        contaDepositoRepository.save(contaPrincipal);

        TransacaoTable transacao = new TransacaoTable();
        transacao.setOperador(operador);
        transacao.setContaDeposito(contaPrincipal);
        if (request.getTipoTransacao().equals(TransacaoTable.TypeTransaction.TRANSFER)) {
            transacao.setContaDestino(contaDestino);
        }
        transacao.setValue(request.getValorTransacao());
        transacao.setTypeMoney(request.getFormaDinheiroTransacao());
        transacao.setTypeTransaction(request.getTipoTransacao());
        transacao.setDescricao(request.getDescricaoTransacao());
        transacao.setSaldoAnterior(saldoAnterior);
        transacao.setSaldoPosterior(contaPrincipal.getTotal_amount());
        transacao.setAuto(auto);

        return transacaoRepository.save(transacao);
    }
}
