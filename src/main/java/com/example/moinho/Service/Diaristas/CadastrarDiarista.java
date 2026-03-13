package com.example.moinho.Service.Diaristas;

import com.example.moinho.Dto.Diaristas.DiaristaRequestDTO;
import com.example.moinho.Dto.Transacao.TransacaoRequestDTO;
import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Entity.Pessoa.Papel;
import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Entity.Transacao;
import com.example.moinho.Exception.BusinessException;
import com.example.moinho.Exception.DiaristaException.CadastroDiaristaException.DiaristaJaCadastradoException;
import com.example.moinho.Exception.DiaristaException.CadastroDiaristaException.PessoaNaoEncontradaException;
import com.example.moinho.Exception.TransacaoExceptions.CadastroTransacaoException.*;
import com.example.moinho.Repository.ContaBaseRepository;
import com.example.moinho.Repository.Pessoa;
import com.example.moinho.Repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CadastrarDiarista {

    private final ContaBaseRepository contaBaseRepository;
    private final TransacaoRepository transacaoRepository;
    private final Pessoa clienteRepository;


    public CadastrarDiarista(ContaBaseRepository contaBaseRepository,
                             TransacaoRepository transacaoRepository,
                             Pessoa clienteRepository) {
        this.contaBaseRepository = contaBaseRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
    }


    public PessoaFisica cadastrarDiarista(DiaristaRequestDTO dto) throws BusinessException {
        try {
            PessoaFisica pessoaF = (PessoaFisica) clienteRepository.findById(dto.getDiaristaId())
                    .orElseThrow(() ->
                            new PessoaNaoEncontradaException("Pessoa não encontrada."));

            if (pessoaF.getPapeis().contains(Papel.DIARISTA)) {
                throw new DiaristaJaCadastradoException("Diarista já cadastrado.");
            }

            pessoaF.getPapeis().add(Papel.DIARISTA);

            return null;
        } catch (Exception e) {
            throw new BusinessException("Erro desconhecido: " + e.getMessage(),
                    "redirect/:Coopase/Servicos/Diarista") {
            };
        }
    }
}
