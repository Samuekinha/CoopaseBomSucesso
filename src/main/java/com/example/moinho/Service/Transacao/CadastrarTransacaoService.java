package com.example.moinho.Service.Transacao;

import com.example.moinho.Dto.TransacaoRequest;
import com.example.moinho.Exception.TransacaoExceptions.ContaDestinoInativaException;
import com.example.moinho.Exception.TransacaoExceptions.ContaDestinoNaoEncontradaException;
import com.example.moinho.Exception.TransacaoExceptions.OperadorNaoEncontradoException;
import com.example.moinho.Exception.TransacaoExceptions.OperadorSemPermissaoException;
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
    public TransacaoTable criarTransacaoEntrada(TransacaoRequest request) {

        // Valida conta de destino
        E_ContaDeposito conta = contaDepositoRepository.findById(request.getContaDestinoId())
                .orElseThrow(() -> new ContaDestinoNaoEncontradaException
                        ("Conta destino não encontrada"));

        if (!conta.isActive()) {
            throw new ContaDestinoInativaException("Conta destino está inativa");
        }

        // Valida operador
        E_Cliente operador = clienteRepository.findById(request.getOperadorTransacaoId())
                .orElseThrow(() -> new OperadorNaoEncontradoException("Operador não encontrado"));

        if (!operador.isOperator()) {
            throw new OperadorSemPermissaoException("Operador não tem permissão");
        }

        // Atualiza saldo da conta
        BigDecimal saldoAnterior = conta.getTotal_amount();
        conta.aplicarDeposito(request.getValorTransacao());
        contaDepositoRepository.save(conta);

        // Cria transação
        TransacaoTable transacao = new TransacaoTable();
        transacao.setOperador(operador);
        transacao.setContaDeposito(conta);
        transacao.setValue(request.getValorTransacao());
        transacao.setTypeMoney(TransacaoTable.TypeMoney.valueOf(request.getFormaDinheiroTransacao()));
        transacao.setDescricao(request.getDescricaoTransacao());
        transacao.setSaldoAnterior(saldoAnterior);
        transacao.setSaldoPosterior(conta.getTotal_amount());

        return transacaoRepository.save(transacao);
    }
}
