package com.example.moinho.Service.Transacao;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.Response.CrudResponse;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Repository.R_Cliente;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CadastrarTransacaoService {
    private final TransacaoRepository transacaoRepo;
    private final ContaDepositoRepository contaDRepo;
    private final R_Cliente clienteRepo;

    public CadastrarTransacaoService(TransacaoRepository transacaoRepo,
                                     ContaDepositoRepository ContaDRepo, R_Cliente rCliente) {
        this.transacaoRepo = transacaoRepo;
        this.contaDRepo = ContaDRepo;
        clienteRepo = rCliente;
    }

    @Transactional
    public CrudResponse cadastrarTransacaoEntrada(BigDecimal valorDeposito,
                                          Long contaDestinoId, Long operadorTransacaoId,
                                          String formaDinheiro, String descricaoTransacao) {
        try {
            // Valida operador da transação
            CrudResponse validadeOperador = validarOperador(operadorTransacaoId);
            if (validadeOperador != null) {
                return validadeOperador;
            }

            // Valida conta de depósito
            CrudResponse validadeContaDeposito = valorContaDeposito(contaDestinoId);
            if (validadeContaDeposito != null) {
                return validadeContaDeposito;
            }

            // Valida valor do depósito
            CrudResponse validadeValorDeposito = validarValor(valorDeposito);
            if (validadeValorDeposito != null) {
                return validadeValorDeposito;
            }

            // Valida forma de entrada (dinheiro, pix ou chque)
            CrudResponse validadeFormaDinheiro = validarFormaDinheiro(formaDinheiro);
            if (validadeFormaDinheiro != null) {
                return validadeFormaDinheiro;
            }

            // Valida descrição
            CrudResponse validadeDescricao = validarDescricao(descricaoTransacao);
            if (validadeDescricao != null) {
                return validadeDescricao;
            }

            // Salvar no banco
            CrudResponse validadeSalvarTransacao = salvarTransacao(valorDeposito,
                    contaDestinoId, operadorTransacaoId, formaDinheiro, descricaoTransacao);
            if (validadeSalvarTransacao != null) {
                return validadeSalvarTransacao;
            }

            return CrudResponse.success("Transação cadastrada com sucesso!");

        } catch (Exception e) {
            return CrudResponse.error("Erro ao cadastrar transação: "
                    + e.getMessage());
        }
    }

    private CrudResponse validarOperador(Long operadorId) {
        if (operadorId == null) {
            return CrudResponse.error("Operador não pode ser nulo.");
        }

        Optional<E_Cliente> operador = clienteRepo.findById(operadorId);

        if (!operador.isPresent()) {
            return CrudResponse.error("Operador não existe.");
        }

        E_Cliente operadorDados = operador.get();
        if (!operadorDados.isOperator()) {
            return CrudResponse.error("Cliente não tem liberação para operadora");
        }

        return null;
    }

    private CrudResponse valorContaDeposito(Long ContaDepositoId) {
        if (ContaDepositoId == null) {
            return CrudResponse.error("Conta de depóstio não pode ser nula.");
        }

        Optional<E_ContaDeposito> contaDeposito = contaDRepo.findById(ContaDepositoId);

        if (!contaDeposito.isPresent()) {
            return CrudResponse.error("A conta de depósito não existe.");
        }

        E_ContaDeposito contaDepositoDados = contaDeposito.get();
        if (!contaDepositoDados.isActive()) {
            return CrudResponse.error("A conta de depósito está inativada!");
        }

        return null;
    }

    private CrudResponse validarValor(BigDecimal valor) {
        if (valor == null) {
            return CrudResponse.error("Valor de depósito não pode ser nulo.");
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return CrudResponse.error("O valor de depósito deve ser maior que R$ 0,00.");
        }

        return null;
    }

    private CrudResponse validarFormaDinheiro(String forma) {
        // Verifica se a forma é nula ou vazia
        if (forma == null || forma.trim().isEmpty()) {
            return CrudResponse.error("Forma de pagamento não pode ser vazia.");
        }

        // Obtém todos os valores válidos do enum TypeMoney
        TransacaoTable.TypeMoney[] formasValidas = TransacaoTable.TypeMoney.values();

        // Verifica se a forma informada existe no enum (case insensitive)
        for (TransacaoTable.TypeMoney tipo : formasValidas) {
            if (tipo.name().equalsIgnoreCase(forma)) {
                return null;
            }
        }

        // Constrói mensagem com formas válidas
        String formasPermitidas = Arrays.stream(formasValidas)
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        return CrudResponse.error("Forma de pagamento inválida. Use: "
                + formasPermitidas);
    }

    private CrudResponse validarDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            return null;
        }

        if (descricao.length() > 255) {
            return CrudResponse.error("A descrição deve ter no maxímo 255 caracteres.");
        }

        return null;
    }

    private CrudResponse salvarTransacao(BigDecimal valorTransacao, Long contaDestinoId,
                     Long operadorTransacaoId, String formaDinheiro, String descricaoTransacao) {

        try {
            TransacaoTable transacaoTable = new TransacaoTable();

            // Tipo de transação (entrada, saída ou transferência)
            transacaoTable.setTypeTransaction(TransacaoTable.TypeTransaction.
                    valueOf("DEPOSIT"));

            // Tipo de dinheiro
            transacaoTable.setTypeMoney(TransacaoTable.TypeMoney.valueOf
                    (formaDinheiro.toUpperCase()));

            Optional<E_ContaDeposito> contaDestino = contaDRepo.findById(contaDestinoId);
            E_ContaDeposito contaDepositoDados = contaDestino.get();

            // Conta de destino
            transacaoTable.setContaDeposito(contaDepositoDados);

            // Valor do depósito
            transacaoTable.setValue(valorTransacao);

            BigDecimal contaDepositoMontante = contaDepositoDados.getTotal_amount();

            // Saldo antes de completar o depósito
            transacaoTable.setSaldoAnterior(contaDepositoMontante);

            // Saldo após completar o depósito
            transacaoTable.
            transacaoTable.setSaldoPosterior(contaDepositoMontante.add(valorTransacao));

            Optional<E_Cliente> operador = clienteRepo.findById(operadorTransacaoId);
            E_Cliente operadorDados = operador.get();

            // Operador do depósito
            transacaoTable.setOperador(operadorDados);

            // Descrição do depósito
            transacaoTable.setDescricao(descricaoTransacao);

            // Salva o pacote no TransacaoTable
            transacaoRepo.save(transacaoTable);

            return null;
        } catch (Exception e) {
            return CrudResponse.error("Erro ao salvar transação: " + e.getMessage());
        }
    }

}