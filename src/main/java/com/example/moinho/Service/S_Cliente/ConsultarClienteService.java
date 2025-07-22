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
        return r_cliente.findAllOrderById();
    }

    public List<E_Cliente> consultarClientePorParametro(String pesquisa) {
        return r_cliente.findAllWithParameterOrderById(pesquisa);
    }

    public List<E_Cliente> consultar10Cooperados() {
        return r_cliente.findCooperatedsLimited(10);
    }

    public List<E_Cliente> consultarTodosCooperados() {
        return r_cliente.findAllCooperateds();
    }

    public List<E_Cliente> consultarVendedores() {
        return r_cliente.findSellersLimited(5);
    }

    public Integer consultarQuantidadeClientes() {
        List<E_Cliente> listaCompleta = r_cliente.findAll();
        List<E_Cliente> listaCompletaVendedores = r_cliente.findAll();
        return listaCompleta.size();
    }

}
