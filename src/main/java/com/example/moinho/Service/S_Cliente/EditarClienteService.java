package com.example.moinho.Service.S_Cliente;

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

    public String validaEditarCliente(Long id, String nome, String documento, LocalDate dataNascimento,
                                      boolean cooperado, LocalDate vencimentoCaf, String codigoCaf) {

        // Busca o cliente existente
        Optional<E_Cliente> clienteOptional = r_cliente.findById(id);
        if (clienteOptional.isEmpty()) {
            return "Erro: Cliente não encontrado com o ID: " + id;
        }

        E_Cliente cliente = clienteOptional.get();

        try {
            // Validação do nome
            String nomeValidado = verificaNome(nome, id);
            if (!nomeValidado.equals(nome)) {
                return nomeValidado;
            }

            // Validação do documento
            if (documento != null) {
                if (!documento.trim().isEmpty()) {
                    documento = null;
                }
                String docValidado = verificaDocumento(documento, id);
                if (!docValidado.equals(documento)) {
                    return docValidado;
                }
            }

            // Validação da data de nascimento
            if (dataNascimento != null) {
                if (dataNascimento.isAfter(LocalDate.now())) {
                    return "Erro: Data de nascimento não pode ser futura";
                }
            }

            // Validação para cooperados
            if (cooperado) {
                if (vencimentoCaf != null) {
                    if (vencimentoCaf.isBefore(LocalDate.now())) {
                        return "Erro: Validade CAF não pode ser no passado";
                    }
                }
                if (codigoCaf != null) {
                    if (codigoCaf.length() > TAMANHO_MAXIMO_CAF) {
                        return "Erro: Código CAF deve ter no máximo " + TAMANHO_MAXIMO_CAF + " caracteres";
                    }
                }
            }

        } catch (Exception e) {
            return "Erro durante o cadastro: " + e.getMessage();
        }

        // Verifica se os dados são diferentes dos atuais
        boolean dadosAlterados = false;

        if (!nome.equals(cliente.getName())) {
            cliente.setName(nome);
            dadosAlterados = true;
        }

        if (!documento.equals(cliente.getDocument())) {
            // Aqui você pode adicionar validação específica de CPF/CNPJ se necessário
            cliente.setDocument(documento);
            dadosAlterados = true;
        }

        if (dataNascimento != null) {
            if (!dataNascimento.equals(cliente.getBirthDate())) {
                cliente.setBirthDate(dataNascimento);
                dadosAlterados = true;
            }
        }

        if (cooperado != cliente.isCooperated()) {
            cliente.setCooperated(cooperado);
            dadosAlterados = true;
        }

        // Atualiza campos específicos de cooperado
        if (cooperado) {
            if (vencimentoCaf != null) {
                if (!vencimentoCaf.equals(cliente.getMaturity_caf())) {
                    cliente.setMaturity_caf(vencimentoCaf);
                    dadosAlterados = true;
                }
            }

            if (!codigoCaf.equals(cliente.getCaf())) {
                cliente.setCaf(codigoCaf);
                dadosAlterados = true;
            }
        } else {
            // Limpa campos de cooperado se o cliente deixou de ser cooperado
            if (cliente.getMaturity_caf() != null || cliente.getCaf() != null) {
                cliente.setMaturity_caf(null);
                cliente.setCaf(null);
                dadosAlterados = true;
            }
        }

        // Se nenhum dado foi alterado, retorna mensagem
        if (!dadosAlterados) {
            return "Nenhuma alteração foi detectada nos dados do cliente.";
        }

        try {
            // Salva as alterações
            r_cliente.save(cliente);
            return "Sucesso ao editar o cliente!"; // Retorno null indica sucesso
        } catch (Exception e) {
            return "Erro ao atualizar cliente: " + e.getMessage();
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

    // Validações
    private String verificaNome(String nome, Long clienteId) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Erro: Nome é obrigatório";
        }

        if (nome.length() > TAMANHO_MAXIMO_NOME) {
            return "Erro: Nome deve ter no máximo " + TAMANHO_MAXIMO_NOME + " caracteres";
        }

        if (contemNumeros(nome)) {
            return "Erro: Nome não pode conter números";
        }

        // Busca cliente com o documento
        Optional<E_Cliente> clienteExistente = r_cliente.findByName(nome);

        // Se encontrou um cliente com esse documento E não é o mesmo cliente sendo editado
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            return "Erro: Nome já cadastrado";
        }

        return nome;
    }

    private String verificaDocumento(String documento, Long clienteId) {
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

        // Busca cliente com o documento
        Optional<E_Cliente> clienteExistente = r_cliente.findByDocument(documento);

        // Se encontrou um cliente com esse documento E não é o mesmo cliente sendo editado
        if (clienteExistente.isPresent() && !clienteExistente.get().getId().equals(clienteId)) {
            return "Erro: Documento já cadastrado";
        }

        return documento;
    }

}