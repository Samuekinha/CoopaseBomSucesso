package com.example.moinho.Service.Transacao;

import com.example.moinho.Dto.Transacao.Resumo.TransacaoResumoDTO;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Model.TransacaoTable;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ConsultarTransacaoService {

    private final ContaDepositoRepository r_ContaD;
    private final TransacaoRepository transacaoRepository;

    public ConsultarTransacaoService(ContaDepositoRepository r_ContaD, TransacaoRepository transacaoRepository) {
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
