package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarClienteService {

    private final R_Cliente r_cliente;

    public ConsultarClienteService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    public List<E_Cliente> consultarCliente() {
        return r_cliente.findAll();
    }

    public List<E_Cliente> consultarCooperados() {
        return r_cliente.findByCooperatedTrue();
    }

}
