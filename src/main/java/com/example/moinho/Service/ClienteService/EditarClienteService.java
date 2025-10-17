package com.example.moinho.Service.ClienteService;

import com.example.moinho.Entity.Pessoa.PessoaBase;
import com.example.moinho.Entity.Pessoa.PessoaFisica;
import com.example.moinho.Entity.Pessoa.PessoaJuridica;
import com.example.moinho.Repository.Pessoa;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class EditarClienteService {

    private final Pessoa pessoa;

    public EditarClienteService(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    private static final int TAMANHO_MAXIMO_NOME_FISICA = 40;
    private static final int TAMANHO_MAXIMO_NOME_JURIDICA = 60;
    private static final int TAMANHO_MAXIMO_CAF = 50;
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;
    private static final Pattern NUMEROS_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern LETRAS_PATTERN = Pattern.compile(".*[a-zA-Zá-úÁ-Ú].*");

    public String editarCliente(Long id, String nome, String documento, LocalDate dataNascimento,
                                boolean cooperado, LocalDate vencimentoCaf, String codigoCaf) {

        if (id == null) return "Erro: ID do cliente não pode ser nulo";

        Optional<PessoaBase> clienteOptional = pessoa.findById(id);
        if (clienteOptional.isEmpty()) return "Erro: Cliente não encontrado com o ID: " + id;

        PessoaBase pessoa = clienteOptional.get();
        boolean dadosAlterados = false;

        // Pessoa Física
        if (pessoa instanceof PessoaFisica pf) {

            // Validações
            if (nome != null && !nome.trim().isEmpty()) {
                String erroNome = validarNome(nome, id, TAMANHO_MAXIMO_NOME_FISICA);
                if (erroNome != null) return erroNome;
            }
            if (documento != null && !documento.trim().isEmpty()) {
                String erroDoc = validarDocumento(documento, id, TAMANHO_CPF);
                if (erroDoc != null) return erroDoc;
            }
            if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now()))
                return "Erro: Data de nascimento não pode ser futura";

            // Aplicar alterações
            if (nome != null && !nome.equals(pf.getNome())) {
                pf.setNome(nome.trim());
                dadosAlterados = true;
            }
            if (documento != null && !documento.equals(pf.getCpf())) {
                pf.setCpf(documento.trim());
                dadosAlterados = true;
            }
            if (dataNascimento != null && !dataNascimento.equals(pf.getData_nascimento())) {
                pf.setData_nascimento(dataNascimento);
                dadosAlterados = true;
            }

            if (cooperado) {
                if (vencimentoCaf != null && !vencimentoCaf.equals(pf.getData_nascimento())) {
                    pf.setData_nascimento(vencimentoCaf);
                    dadosAlterados = true;
                }
                if (codigoCaf != null) {
                    // adicionar campo CAF se tiver na entidade
                }
            }

            if (dadosAlterados) this.pessoa.save(pf);
            return dadosAlterados ? "Sucesso ao editar o cliente '" + pf.getNome() + "'!"
                    : "Nenhuma alteração foi detectada nos dados do cliente.";

        }

        // Pessoa Jurídica
        else if (pessoa instanceof PessoaJuridica pj) {

            // Validações
            if (nome != null && !nome.trim().isEmpty()) {
                String erroNome = validarNome(nome, id, TAMANHO_MAXIMO_NOME_JURIDICA);
                if (erroNome != null) return erroNome;
            }
            if (documento != null && !documento.trim().isEmpty()) {
                String erroCnpj = validarCnpj(documento, id);
                if (erroCnpj != null) return erroCnpj;
            }
            if (cooperado) {
                if (vencimentoCaf != null && vencimentoCaf.isBefore(LocalDate.now()))
                    return "Erro: Validade CAF não pode ser no passado";
                if (codigoCaf != null && codigoCaf.length() > TAMANHO_MAXIMO_CAF)
                    return "Erro: Código CAF deve ter no máximo " + TAMANHO_MAXIMO_CAF + " caracteres";
            }

            // Aplicar alterações
            if (nome != null && !nome.equals(pj.getNome())) {
                pj.setNome(nome.trim());
                dadosAlterados = true;
            }
            if (documento != null && !documento.equals(pj.getCnpj())) {
                pj.setCnpj(documento.trim());
                dadosAlterados = true;
            }
            if (cooperado) {
                if (vencimentoCaf != null && !vencimentoCaf.equals(pj.getValidade_caf_juridica())) {
                    pj.setValidade_caf_juridica(vencimentoCaf);
                    dadosAlterados = true;
                }
                if (codigoCaf != null && !codigoCaf.equals(pj.getCaf_juridica())) {
                    pj.setCaf_juridica(codigoCaf);
                    dadosAlterados = true;
                }
            }

            if (dadosAlterados) this.pessoa.save(pj);
            return dadosAlterados ? "Sucesso ao editar o cliente '" + pj.getNome() + "'!"
                    : "Nenhuma alteração foi detectada nos dados do cliente.";
        }

        return "Erro: Tipo de pessoa não suportado";
    }

    private String validarNome(String nome, Long clienteId, int tamanhoMaximo) {
        nome = nome.trim();
        if (nome.length() > tamanhoMaximo) return "Erro: Nome deve ter no máximo " + tamanhoMaximo + " caracteres";
        if (contemNumeros(nome)) return "Erro: Nome não pode conter números";
        Optional<PessoaBase> clienteExistente = pessoa.findByNome(nome);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId))
            return "Erro: Nome já cadastrado";
        return null;
    }

    private String validarDocumento(String documento, Long clienteId, int tamanhoCpf) {
        documento = documento.trim();
        if (contemLetras(documento)) return "Erro: Documento não pode conter letras";
        if (documento.length() != TAMANHO_CPF && documento.length() != TAMANHO_CNPJ)
            return "Erro: Documento deve ter " + TAMANHO_CPF + " (CPF) ou " + TAMANHO_CNPJ + " (CNPJ) caracteres";
        Optional<PessoaFisica> clienteExistente = pessoa.findByCpf(documento);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId))
            return "Erro: Documento já cadastrado";
        return null;
    }

    private String validarCnpj(String cnpj, Long clienteId) {
        return validarDocumento(cnpj, clienteId, TAMANHO_CNPJ);
    }

    private boolean contemNumeros(String texto) {
        return texto != null && NUMEROS_PATTERN.matcher(texto).matches();
    }

    private boolean contemLetras(String texto) {
        return texto != null && LETRAS_PATTERN.matcher(texto).matches();
    }
}
