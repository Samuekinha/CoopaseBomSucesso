package com.example.moinho.Service.CofreService;

import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Response.OperationResult;
import com.example.moinho.Repository.Pessoa;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarContaDepositoService {

    private final Pessoa pessoa;

    public DeletarContaDepositoService(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public OperationResult DeletarContaDeposito(Long id) {

        try {
            Optional<PessoaBase> clienteParaDeletar = pessoa.findById(id);

            if (clienteParaDeletar.isPresent()) {
                pessoa.deleteById(id);
                return OperationResult.success("Deletado com Sucesso!");
            } else {
                return OperationResult.error("Erro: Cliente não foi encontrado!");
            }

        } catch (Exception e) {
            return OperationResult.error("Erro ao deletar: " + e.getMessage());
        }
    }

}
