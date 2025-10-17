package com.example.moinho.Service.Transacao;

import com.example.moinho.Dto.Transacao.TransacaoRequestDTO;
import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Entity.Pessoa.Papel;
import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.*;
import com.example.moinho.Entity.Transacao;
import com.example.moinho.Repository.ContaBaseRepository;
import com.example.moinho.Repository.Pessoa;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CadastrarTransacaoService {

    private final ContaBaseRepository contaBaseRepository;
    private final TransacaoRepository transacaoRepository;
    private final Pessoa clienteRepository;

    public CadastrarTransacaoService(ContaBaseRepository contaBaseRepository,
                                     TransacaoRepository transacaoRepository,
                                     Pessoa clienteRepository) {
        this.contaBaseRepository = contaBaseRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Transacao criarTransacao(TransacaoRequestDTO request, boolean auto) {

        ContaBase contaPrincipal = contaBaseRepository.findById(request.getContaPrincipalId())
                .orElseThrow(() -> new ContaPrincipalNaoEncontradaException("Conta principal não encontrada"));

        if (!contaPrincipal.isAtiva()) {
            throw new ContaPrincipalInativaException("Conta principal está inativa");
        }

        ContaBase contaDestino = null;
        if (request.getTipoTransacao().equals(Transacao.TypeTransaction.TRANSFERENCIA)) {
            contaDestino = contaBaseRepository.findById(request.getContaDestinoId())
                    .orElseThrow(() -> new ContaDestinoNaoEncontradaException("Conta destino não encontrada"));

            if (!contaDestino.isAtiva()) {
                throw new ContaDestinoInativaException("Conta destino está inativa");
            }
        }

        PessoaBase operador = clienteRepository.findById(request.getOperadorTransacaoId())
                .orElseThrow(() -> new OperadorNaoEncontradoException("Operador não encontrado"));

        if (operador instanceof PessoaFisica pf) {
            if (!pf.getPapeis().contains(Papel.OPERADOR)) {
                throw new OperadorSemPermissaoException("Operador não tem permissão");
            }
        } else {
            throw new OperadorSemPermissaoException("Apenas PessoaFisica pode operar");
        }


        BigDecimal saldoAnterior = contaPrincipal.getValor_total();

        switch (request.getTipoTransacao()) {
            case DEPOSITO -> contaPrincipal.aplicarTransacao(request.getValorTransacao(), Transacao.TypeTransaction.DEPOSITO);
            case SAQUE -> contaPrincipal.aplicarTransacao(request.getValorTransacao(), Transacao.TypeTransaction.SAQUE);
            case TRANSFERENCIA -> {
                contaPrincipal.aplicarTransacao(request.getValorTransacao(), Transacao.TypeTransaction.SAQUE);
                assert contaDestino != null;
                contaDestino.aplicarTransacao(request.getValorTransacao(), Transacao.TypeTransaction.DEPOSITO);
                contaBaseRepository.save(contaDestino);
            }
        }

        contaBaseRepository.save(contaPrincipal);

        Transacao transacao = new Transacao();
        transacao.setOperador(operador);
        transacao.setConta_principal(contaPrincipal);
        if (request.getTipoTransacao().equals(Transacao.TypeTransaction.TRANSFERENCIA)) {
            transacao.setConta_destino(contaDestino);
        }
        transacao.setValor(request.getValorTransacao());
        transacao.setTipo_dinheiro(request.getFormaDinheiroTransacao());
        transacao.setTipo_transacao(request.getTipoTransacao());
        transacao.setDescricao(request.getDescricaoTransacao());
        transacao.setSaldo_anterior(saldoAnterior);
        transacao.setSaldo_posterior(contaPrincipal.getValor_total());
        transacao.setAutomatica(auto);

        return transacaoRepository.save(transacao);
    }
}
