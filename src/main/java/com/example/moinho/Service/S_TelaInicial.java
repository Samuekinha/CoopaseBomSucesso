package com.example.moinho.Service;

import com.example.moinho.Service.S_Cliente.S_CadastroCliente;
import com.example.moinho.Service.S_Despesa.S_CadastroDespesa;
import org.springframework.stereotype.Service;

@Service
public class S_TelaInicial {
    private final S_CadastroCliente s_cadastroCliente;
    private final S_CadastroDespesa s_cadastroDespesa;

    public S_TelaInicial(S_CadastroCliente s_cadastroCliente, S_CadastroDespesa s_cadastroDespesa) {
        this.s_cadastroCliente = s_cadastroCliente;
        this.s_cadastroDespesa = s_cadastroDespesa;
    }

    // Metodo principal (logica de direcionamento)
    public String direcionador(int[] Area, int[] Servico) {
        switch (Area[0]) {
            case 1 -> { // Área Cliente
                switch (Servico[1]) {
                    case 1 -> s_cadastroCliente.cadastrarCliente();
                    // Outros cases...
                }
            }
            case 4 -> { // Área Despesa
                switch (Servico[1]) {
                    //case 1 -> s_cadastroDespesa.f();
                    // Outros cases...
                }
            }
        }
        return "Operação concluída";
    }

    // Métodos auxiliares (validação, etc.)
    public boolean validaEscolha(int input, int quantidadeEscolhas) {
        return input > 0 && input <= quantidadeEscolhas;
    }
}