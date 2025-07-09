package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

@Service
public class EditarClienteService {
    private R_Cliente r_cliente;

    public EditarClienteService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }



}
