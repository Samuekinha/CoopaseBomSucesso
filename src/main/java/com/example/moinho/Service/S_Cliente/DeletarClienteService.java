package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarClienteService {

    private final R_Cliente r_cliente;

    public DeletarClienteService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    public String validaDeletarCliente(Long id) {

        try {
            Optional<E_Cliente> clienteParaDeletar = r_cliente.findById(id);

            if (clienteParaDeletar.isPresent()) {
                r_cliente.deleteById(id);
                return "Deletado com Sucesso!";
            } else {
                return "Erro: Cliente não foi encontrado!";
            }

        } catch (Exception e) {
            return "Erro ao deletar: " + e.getMessage();
        }
    }

}
