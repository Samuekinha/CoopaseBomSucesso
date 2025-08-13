package com.example.moinho.Service.CofreService;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.Response.CrudResponse;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarContaDepositoService {

    private final R_Cliente r_cliente;

    public DeletarContaDepositoService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    public CrudResponse DeletarContaDeposito(Long id) {

        try {
            Optional<E_Cliente> clienteParaDeletar = r_cliente.findById(id);

            if (clienteParaDeletar.isPresent()) {
                r_cliente.deleteById(id);
                return CrudResponse.success("Deletado com Sucesso!");
            } else {
                return CrudResponse.error("Erro: Cliente não foi encontrado!");
            }

        } catch (Exception e) {
            return CrudResponse.error("Erro ao deletar: " + e.getMessage());
        }
    }

}
