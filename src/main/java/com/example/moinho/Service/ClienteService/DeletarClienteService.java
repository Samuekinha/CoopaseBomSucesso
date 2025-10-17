package com.example.moinho.Service.ClienteService;

import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Repository.Pessoa;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarClienteService {

    private final Pessoa pessoa;

    public DeletarClienteService(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String validaDeletarCliente(Long id) {

        try {
            Optional<PessoaBase> clienteParaDeletar = pessoa.findById(id);

            if (clienteParaDeletar.isPresent()) {
                pessoa.deleteById(id);
                return "Deletado com Sucesso!";
            } else {
                return "Erro: Cliente não foi encontrado!";
            }

        } catch (Exception e) {
            return "Erro ao deletar: " + e.getMessage();
        }
    }

}
