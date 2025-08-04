package com.example.moinho.Service.ClienteService;

import com.example.moinho.Model.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class EditarClienteService {
    private final R_Cliente r_cliente;

    public EditarClienteService(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
    }

    private static final int TAMANHO_MAXIMO_NOME = 40;
    private static final int TAMANHO_MAXIMO_CAF = 40;
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;
    private static final Pattern NUMEROS_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern LETRAS_PATTERN = Pattern.compile(".*[a-zA-Zá-úÁ-Ú].*");

    public String editarCliente(Long id, String nome, String documento, LocalDate dataNascimento,
                                boolean cooperado, LocalDate vencimentoCaf, String codigoCaf) {

        // Validações preliminares antes de buscar no banco
        if (id == null) {
            return "Erro: ID do cliente não pode ser nulo";
        }

        try {
            // Validação do nome
            if (nome != null && !nome.trim().isEmpty()) {
                String erroNome = validarNome(nome, id);
                if (erroNome != null) return erroNome;
            }

            // Validação do documento
            if (documento != null && !documento.trim().isEmpty()) {
                String erroDoc = validarDocumento(documento, id);
                if (erroDoc != null) return erroDoc;
            }

            // Validação da data de nascimento
            if (dataNascimento != null && dataNascimento.isAfter(LocalDate.now())) {
                return "Erro: Data de nascimento não pode ser futura";
            }

            // Validação para cooperados
            if (cooperado) {
                if (vencimentoCaf != null && vencimentoCaf.isBefore(LocalDate.now())) {
                    return "Erro: Validade CAF não pode ser no passado";
                }
                if (codigoCaf != null && codigoCaf.length() > TAMANHO_MAXIMO_CAF) {
                    return "Erro: Código CAF deve ter no máximo " + TAMANHO_MAXIMO_CAF + " caracteres";
                }
            }

            // Busca o cliente existente após as validações básicas
            Optional<E_Cliente> clienteOptional = r_cliente.findById(id);
            if (clienteOptional.isEmpty()) {
                return "Erro: Cliente não encontrado com o ID: " + id;
            }

            E_Cliente cliente = clienteOptional.get();
            boolean dadosAlterados = false;

            // Aplicar alterações
            if (nome != null && !nome.trim().isEmpty() && !nome.equals(cliente.getName())) {
                cliente.setName(nome.trim());
                dadosAlterados = true;
            }

            if (documento != null && !documento.trim().isEmpty() && !documento.equals(cliente.getDocument())) {
                cliente.setDocument(documento.trim());
                dadosAlterados = true;
            }

            if (dataNascimento != null && !dataNascimento.equals(cliente.getBirthDate())) {
                cliente.setBirthDate(dataNascimento);
                dadosAlterados = true;
            }

            if (cooperado != cliente.isCooperated()) {
                cliente.setCooperated(cooperado);
                dadosAlterados = true;
            }

            // Campos específicos de cooperado
            if (cooperado) {
                if (vencimentoCaf != null && !vencimentoCaf.equals(cliente.getMaturity_caf())) {
                    cliente.setMaturity_caf(vencimentoCaf);
                    dadosAlterados = true;
                }
                if (codigoCaf != null && !codigoCaf.equals(cliente.getCaf())) {
                    cliente.setCaf(codigoCaf);
                    dadosAlterados = true;
                }
            } else {
                // Limpa campos se não é mais cooperado
                if (cliente.getMaturity_caf() != null || cliente.getCaf() != null) {
                    cliente.setMaturity_caf(null);
                    cliente.setCaf(null);
                    dadosAlterados = true;
                }
            }

            if (!dadosAlterados) {
                return "Nenhuma alteração foi detectada nos dados do cliente.";
            }

            r_cliente.save(cliente);
            return "Sucesso ao editar o cliente '" + cliente.getName() + "'!";

        } catch (Exception e) {
            return "Erro ao atualizar cliente: " + e.getMessage();
        }
    }

    private String validarNome(String nome, Long clienteId) {
        nome = nome.trim();

        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        if (contemNumeros(nome)) {
            return "Erro: Nome não pode conter números";
        }

        Optional<E_Cliente> clienteExistente = r_cliente.findByName(nome);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            return "Erro: Nome já cadastrado";
        }

        return null;
    }

    private String validarDocumento(String documento, Long clienteId) {
        documento = documento.trim();

        if (contemLetras(documento)) {
            return "Erro: Documento não pode conter letras";
        }

        if (documento.length() != TAMANHO_CPF && documento.length() != TAMANHO_CNPJ) {
            return "Erro: Documento deve ter " + TAMANHO_CPF + " (CPF) ou " + TAMANHO_CNPJ + " (CNPJ) caracteres";
        }

        Optional<E_Cliente> clienteExistente = r_cliente.findByDocument(documento);
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            return "Erro: Documento já cadastrado";
        }

        return null;
    }

    private boolean contemNumeros(String texto) {
        return texto != null && NUMEROS_PATTERN.matcher(texto).matches();
    }

    private boolean contemLetras(String texto) {
        return texto != null && LETRAS_PATTERN.matcher(texto).matches();
    }
}