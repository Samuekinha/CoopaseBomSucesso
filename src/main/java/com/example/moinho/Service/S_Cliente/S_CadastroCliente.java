package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class S_CadastroCliente {
    private final R_Cliente r_cliente;

    public S_CadastroCliente(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    // Constantes de configuração
    private static final int TAMANHO_MAXIMO_NOME = 40;
    private static final int TAMANHO_MAXIMO_CAF = 40;
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;

    public String[] cadastrarCliente(String nome, String documento, LocalDate dataNascimento,
                                   Boolean cooperado, LocalDate validadeCaf, String codigoCaf) {
        String[] resultados = new String[6];

        try {
            // Validação do nome
            String nomeValidado = verificaNome(nome);
            if (!nomeValidado.equals(nome)) {
                resultados[0] = nomeValidado;
            }

            // Validação do documento
            if (documento != null && !documento.isEmpty()) {
                String docValidado = verificaDocumento(documento);
                if (!docValidado.equals(documento)) {
                    resultados[1] = docValidado;
                }
            }

            // Validação da data de nascimento
            if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
                resultados[2] = "Erro: Data de nascimento não pode ser futura";
            }

            // Validação para cooperados
            if (cooperado) {
                if (validadeCaf == null) {
                    resultados[3] = "Erro: Validade CAF é obrigatória para cooperados";
                }
                if (validadeCaf.isBefore(LocalDate.now())) {
                    resultados[3] = "Erro: Validade CAF não pode ser no passado";
                }
                if (codigoCaf == null || codigoCaf.isEmpty()) {
                    resultados[4] = "Erro: Código CAF é obrigatório para cooperados";
                }
                if (codigoCaf.length() > TAMANHO_MAXIMO_CAF) {
                    resultados[4] = "Erro: Código CAF deve ter no máximo " + TAMANHO_MAXIMO_CAF + " caracteres";
                }
            }

            // Salvar cliente
            salvarCliente(nome, documento, dataNascimento, cooperado, validadeCaf, codigoCaf);
            resultados[5] = "Sucesso: Cliente cadastrado!";

        } catch (Exception e) {
            resultados[5] = "Erro durante o cadastro: " + e.getMessage();
        }
        return resultados;
    }

    // Métodos auxiliares
    private static final Pattern NUMEROS_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern LETRAS_PATTERN = Pattern.compile(".*[a-zA-Zá-úÁ-Ú].*");

    private boolean contemNumeros(String texto) {
        return texto != null && NUMEROS_PATTERN.matcher(texto).matches();
    }

    private boolean contemLetras(String texto) {
        return texto != null && LETRAS_PATTERN.matcher(texto).matches();
    }

    // Validações
    private String verificaNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Erro: Nome é obrigatório";
        }

        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        if (contemNumeros(nome)) {
            return "Erro: Nome não pode conter números";
        }

        if (r_cliente.findByName(nome).isPresent()) {
            return "Erro: Nome já cadastrado";
        }

        return nome;
    }

    private String verificaDocumento(String documento) {
        if (documento == null || documento.isEmpty()) {
            return "Erro: Documento é obrigatório";
        }

        if (contemLetras(documento)) {
            return "Erro: Documento não pode conter letras";
        }

        if (documento.length() != TAMANHO_CPF && documento.length() != TAMANHO_CNPJ) {
            return "Erro: Documento deve ter " + TAMANHO_CPF + " (CPF) ou " + TAMANHO_CNPJ +
                    " (CNPJ) caracteres";
        }

        if (r_cliente.findByDocument(documento).isPresent()) {
            return "Erro: Documento já cadastrado";
        }

        return documento;
    }

    private void salvarCliente(String nome, String documento, LocalDate dataNascimento,
                               boolean cooperado, LocalDate validadeCaf, String codigoCaf) {
        E_Cliente cliente = new E_Cliente();
        cliente.setName(nome);
        cliente.setDocument(documento);
        cliente.setBirthDate(dataNascimento);
        cliente.setCooperated(cooperado);

        if (cooperado) {
            cliente.setMaturity_caf(validadeCaf);
            cliente.setCaf(codigoCaf);
        }

        r_cliente.save(cliente);
    }
}