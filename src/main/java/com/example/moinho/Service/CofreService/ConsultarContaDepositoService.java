package com.example.moinho.Service.CofreService;

import com.example.moinho.Entity.ContaDeposito.ContaBase;
import com.example.moinho.Repository.ContaBaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultarContaDepositoService {

    private final ContaBaseRepository r_ContaD;

    public ConsultarContaDepositoService(ContaBaseRepository r_ContaD) {
        this.r_ContaD = r_ContaD;
    }

    public List<ContaBase> consultarTodasContaDeposito() {
        return r_ContaD.findTodasContaDeposito();
    }

    // Metodo alternativo para buscar apenas contas ativas
    public List<ContaBase> consultarContasAtivas() {
        return r_ContaD.findTodasContaDeposito().stream()
                .filter(ContaBase::isAtiva)
                .toList();
    }

    public List<ContaBase> consultarContasInativas() {
        return r_ContaD.findTodasContaDeposito().stream()
                .filter(conta -> !conta.isAtiva())
                .toList();
    }

    public BigDecimal ConsultarValorTotalContas() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ContaBase> todasContas = r_ContaD.findTodasContaDeposito();

        for (ContaBase conta : todasContas) {
            if (conta.getValor_total() != null) {
                valorTotal = valorTotal.add(conta.getValor_total());
            }
        }

        return valorTotal;
    }

    public Optional<ContaBase> consultarContaPorId (Long id) {
        return r_ContaD.findById(id);
    }
}
