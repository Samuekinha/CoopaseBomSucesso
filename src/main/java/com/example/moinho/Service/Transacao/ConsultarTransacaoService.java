package com.example.moinho.Service.Transacao;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Repository.ContaBaseRepository;
import com.example.moinho.Repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarTransacaoService {

    private final ContaBaseRepository r_ContaD;
    private final TransacaoRepository transacaoRepository;

    public ConsultarTransacaoService(ContaBaseRepository r_ContaD, TransacaoRepository transacaoRepository) {
        this.r_ContaD = r_ContaD;
        this.transacaoRepository = transacaoRepository;
    }

    public List<TransacaoResumoDTO> consultarTodasTransacao() {
        return transacaoRepository.buscarResumo();
    }

    public List<TransacaoResumoDTO> consultarTodasTransacaoAtivas() {
        return transacaoRepository.buscarResumoAtivas();
    }

    public List<TransacaoResumoDTO> consultarTodasTransacaoInativas() {
        return transacaoRepository.buscarResumoInativas();
    }

}
