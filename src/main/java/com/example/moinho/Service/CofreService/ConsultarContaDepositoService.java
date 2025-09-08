package com.example.moinho.Service.CofreService;

import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Repository.ContaDepositoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultarContaDepositoService {

    private final ContaDepositoRepository r_ContaD;

    public ConsultarContaDepositoService(ContaDepositoRepository r_ContaD) {
        this.r_ContaD = r_ContaD;
    }

    public List<E_ContaDeposito> consultarTodasContaDeposito() {
        return r_ContaD.findAllContasD();
    }

    // Metodo alternativo para buscar apenas contas ativas
    public List<E_ContaDeposito> consultarContasAtivas() {
        return r_ContaD.findAllContasD().stream()
                .filter(E_ContaDeposito::isActive)
                .toList();
    }

    public BigDecimal ConsultarValorTotalContas() {
        BigDecimal valorTotal = BigDecimal.ZERO;
        List<E_ContaDeposito> todasContas = r_ContaD.findAllContasD();

        for (E_ContaDeposito conta : todasContas) {
            if (conta.getTotal_amount() != null) {
                valorTotal = valorTotal.add(conta.getTotal_amount());
            }
        }

        return valorTotal;
    }

    public Optional<E_ContaDeposito> consultarContaPorId (Long id) {
        return r_ContaD.findById(id);
    }
}
