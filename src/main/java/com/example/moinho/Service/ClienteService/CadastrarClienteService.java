package com.example.moinho.Service.ClienteService;

import com.example.moinho.Entity.Pessoa.Papel;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Repository.Pessoa;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
public class CadastrarClienteService {
    private final Pessoa pessoa;

    public CadastrarClienteService(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    // Constantes de configuração
    private static final int TAMANHO_MAXIMO_NOME = 40;
    private static final int TAMANHO_MAXIMO_CAF = 40;
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;

    public String cadastrarCliente(String nome, String cpf, LocalDate dataNascimento,
                                   boolean cooperado, LocalDate validadeCaf, String codigoCaf,
                                   boolean operatorSelect, boolean sellerSelect) {
        try {
            // Validação do nome
            String erroNome = validarNome(nome);
            if (erroNome != null) {
                return erroNome;
            }

            // Validação do documento
            if (cpf != null && !cpf.trim().isEmpty()) {
                String erroCpf = validarCpf(cpf);
                if (erroCpf != null) {
                    return erroCpf;
                }
            }

            // Validação da data de nascimento
            if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
                return "Erro: Data de nascimento não pode ser futura";
            }

            // Validação para cooperados
            if (cooperado) {
                if  (validadeCaf != null) {
                    if (validadeCaf.isBefore(LocalDate.now())) {
                        return "Erro: Validade CAF não pode ser no passado";
                    }
                }

                if (codigoCaf != null) {
                    if (codigoCaf.length() > TAMANHO_MAXIMO_CAF) {
                        return "Erro: Código CAF deve ter no máximo " + TAMANHO_MAXIMO_CAF + " caracteres";
                    }
                }
            }

            // Salvar cliente
            PessoaFisica pf = new PessoaFisica();
            pf.setNome(nome.trim());
            pf.setCpf(cpf.trim());
            pf.setData_nascimento(dataNascimento);
            if (cooperado) {
                pf.getPapeis().add(Papel.COOPERADO);
                pf.setCaf_fisica(codigoCaf);
                pf.setValidade_caf_fisica(validadeCaf);
            }
            if (operatorSelect) {
                pf.getPapeis().add(Papel.OPERADOR);
            }
            if (sellerSelect) {
                pf.getPapeis().add(Papel.VENDEDOR);
            }

            pessoa.save(pf);
            return "Sucesso: Cliente cadastrado com sucesso!";

        } catch (Exception e) {
            return "Erro durante o cadastro: " + e.getMessage();
        }
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

    private String validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Erro: Nome é obrigatório";
        }

        nome = nome.trim();
        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        if (contemNumeros(nome)) {
            return "Erro: Nome não pode conter números";
        }

        if (pessoa.findByNome(nome).isPresent()) {
            return "Erro: Nome já cadastrado";
        }

        return null;
    }

    private String validarCpf(String documento) {
        documento = documento.trim();
        if (contemLetras(documento)) {
            return "Erro: Documento não pode conter letras";
        }

        if (documento.length() != TAMANHO_CPF && documento.length() != TAMANHO_CNPJ) {
            return "Erro: Documento deve ter " + TAMANHO_CPF + " (CPF) ou " + TAMANHO_CNPJ + " (CNPJ) caracteres";
        }

        if (pessoa.findByCpf(documento).isPresent()) {
            return "Erro: Documento já cadastrado";
        }

        return null;
    }
}