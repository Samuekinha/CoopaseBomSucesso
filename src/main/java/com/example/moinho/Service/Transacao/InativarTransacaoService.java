package com.example.moinho.Service.Transacao;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Model.Response.OperationResult;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InativarTransacaoService {

    private final R_Cliente r_cliente;

    public InativarTransacaoService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    public OperationResult DeletarContaDeposito(Long id) {

        try {
            Optional<E_Cliente> clienteParaDeletar = r_cliente.findById(id);

            if (clienteParaDeletar.isPresent()) {
                r_cliente.deleteById(id);
                return OperationResult.success("Deletado com Sucesso!");
            } else {
                return OperationResult.error("Erro: Cliente não foi encontrado!");
            }

        } catch (Exception e) {
            return OperationResult.error("Erro ao deletar: " + e.getMessage());
        }
    }

}
