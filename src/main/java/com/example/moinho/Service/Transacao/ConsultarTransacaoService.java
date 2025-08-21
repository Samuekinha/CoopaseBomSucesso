package com.example.moinho.Service.Transacao;

import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Repository.ContaDepositoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ConsultarTransacaoService {

    private final ContaDepositoRepository r_ContaD;

    public ConsultarTransacaoService(ContaDepositoRepository r_ContaD) {
        this.r_ContaD = r_ContaD;
    }

    public List<E_ContaDeposito> consultarContaDeposito() {
        return r_ContaD.findAllContasD();
    }
    
    public BigDecimal ConsultarValorTotalContas() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<E_ContaDeposito> todasContas = r_ContaD.findAll();
        
        for (int i = 0; i < todasContas.size(); i++) {
            E_ContaDeposito ContaDepositoValor = todasContas.get(i);
            valorTotal = valorTotal.add(ContaDepositoValor.getTotal_amount());
        }
        
        return valorTotal;
    }

}
