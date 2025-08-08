package com.example.moinho.Service.CofreService;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.E_ContaDeposito;
import com.example.moinho.Repository.ContaDepositoRepository;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultarContaDepositoService {

    private final ContaDepositoRepository r_ContaD;

    public ConsultarContaDepositoService(ContaDepositoRepository r_ContaD) {
        this.r_ContaD = r_ContaD;
    }

    public List<E_ContaDeposito> consultarContaDeposito() {
        return r_ContaD.findAllContasD();
    }

}
