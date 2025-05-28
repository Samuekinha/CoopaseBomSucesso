package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Entities.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import com.example.moinho.Service.S_TelaInicial;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

@Service
public class S_Cadastro {
    private final R_Cliente r_cliente;  // Adicionado final para injeção
    private final Scanner scanner;

    // Construtor recebe apenas R_Cliente (Spring injeta) e cria o Scanner internamente
    public S_Cadastro(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
        this.scanner = new Scanner(System.in); // Cria o Scanner aqui
    }

    public String cadastrarCliente() {
        try {
            //solicitar dados passo a passo
            String nome = solicitarNome();
            if (nome == null) return "Cadastro cancelado.";

            String documento = solicitarDocumento();
            if (documento == null) return "Cadastro cancelado.";

            LocalDate dataNascimento = solicitarDataNascimento();
            if (dataNascimento == null) return "Cadastro cancelado.";

            Boolean cooperado = solicitarCooperado();
            if (cooperado == null) return "Cadastro cancelado";

            LocalDate validadeCaf = solicitarValidadeCaf(cooperado);
            if (validadeCaf == null) return "Cadastro cancelado";

            String codigoCaf = solicitarCodigoCaf(cooperado);
            if (codigoCaf == null) return "Cadastro cancelado";

            // ... salvar os dados

            salvarCliente(nome, documento, dataNascimento, cooperado, validadeCaf, codigoCaf);
            return "Cliente cadastrado!";
        }  catch (Exception e) {
            return "Erro durante o cadastro: " + e.getMessage();
        }
    }

    // Validação do nome
    private String solicitarNome() {
        final String COMANDO_PARADA = "stop";

        while (true) {
            System.out.println("Digite o nome do cliente (ou '" + COMANDO_PARADA + "' para cancelar):");
            String input = scanner.nextLine().trim();

            // Verifica se está vazio
            if (input.isEmpty()) {
                System.out.println("Erro: Nome não pode ser vazio. Tente novamente.");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            if (input.length() > 40) {
                System.out.println("Erro: O nome deve ter menos que 40 caracteres. Tente novamente.");
                continue;
            }

            // Validação do nome
            if (contemNumeros(input)) {
                System.out.println("Erro: Nomes não podem conter números. Tente novamente.");
                continue;
            }

            return input;
        }
    }

    // Validação co CPF ou CNPJ
    private String solicitarDocumento(){
        final String COMANDO_PARADA = "stop";

        while (true) {
            System.out.println("Digite o CPF ou CNPJ do cliente (ou '" + COMANDO_PARADA + "' para cancelar):");
            String input = scanner.nextLine().trim();

            // Verifica se está vazio
            if (input.isEmpty()) {
                System.out.println("Erro: Cpf ou Cnpj não podem ser vazios. Tente novamente.");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            // Validação da identificação
            if (contemLetras(input)) {
                System.out.println("Erro: Cpf ou Cnpj não podem conter letras. Tente novamente.");
                continue;
            }

            // Validação do tamanho
            if (input.length() != 14 && input.length() != 11){
                System.out.println("Erro: O Cpf e Cnpj apenas podem ter 11 ou 14 números. Tente novamente.");
                continue;
            }

            return input;
        }
    }

    // Validação da data de nascimento
    private LocalDate solicitarDataNascimento() {
        final String COMANDO_PARADA = "stop";
        final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("ddMMyyyy");

        while (true) {
            System.out.println("Digite a data de nascimento (ddMMyyyy) ou '" + COMANDO_PARADA +
                    "' para cancelar:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Erro: Data de nascimento não pode ser vazia. Tente novamente.");
                continue;
            }

            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            try {
                if (input.length() != 8) {
                    throw new IllegalArgumentException("Data deve ter exatamente 8 dígitos.");
                }

                LocalDate data = LocalDate.parse(input, FORMATO);

                if (data.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Data não pode ser futura.");
                }

                return data;

            } catch (DateTimeParseException e) {
                System.out.println("Erro: Formato de data inválido. Use ddMMyyyy.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage() + " Tente novamente.");
            }
        }
    }

    private Boolean solicitarCooperado() {
        final String COMANDO_PARADA = "stop";

        while (true) {
            System.out.println("Digite 'sim' se for cooperado, se negativo digite qualquer coisa (ou '"
                    + COMANDO_PARADA + "' para cancelar):");
            String input = scanner.nextLine().trim();

            // Verifica se está vazio
            if (input.isEmpty()) {
                System.out.println("Erro: O campo não pode ser vazio. Tente novamente.");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            // Validação de cooperado
            if (input.equalsIgnoreCase("sim")){
                System.out.println("Cooperado ativado com sucesso!");
                return true;
            }

            return false;
        }
    }

    // Validação da data de nascimento
    private LocalDate solicitarValidadeCaf(Boolean cooperado) {
        final String COMANDO_PARADA = "stop";
        final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("ddMMyyyy");

        while (cooperado) {
            System.out.println("Digite a data de vencimento da CAF (ddMMyyyy) ou '" + COMANDO_PARADA +
                    "' para cancelar:");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Erro: Data de vencimento da CAF não pode ser vazia. Tente novamente.");
                continue;
            }

            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            try {
                if (input.length() != 8) {
                    throw new IllegalArgumentException("Data de vencimento deve ter exatamente 8 dígitos.");
                }

                LocalDate data = LocalDate.parse(input, FORMATO);

                if (data.isBefore(LocalDate.now())) {
                    throw new IllegalArgumentException("Data de vencimento não pode ser passado.");
                }

                return data;

            } catch (DateTimeParseException e) {
                System.out.println("Erro: Formato de data inválido. Use ddMMyyyy.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage() + " Tente novamente.");
            }
        }
        return null;
    }

    // Validação do código de CAF
    private String solicitarCodigoCaf(Boolean cooperado) {
        final String COMANDO_PARADA = "stop";

        while (cooperado) {
            System.out.println("Digite o código da CAF do cliente (ou '" + COMANDO_PARADA +
                    "' para cancelar):");
            String input = scanner.nextLine().trim();

            // Verifica se está vazio
            if (input.isEmpty()) {
                System.out.println("Erro: Código de CAF não pode ser vazio.");
                continue;
            }

            if (input.length() > 40) {
                System.out.println("Erro: O código da CAF não pode conter mais que 40 caracteres. Tente novamente");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            return input;
        }
        return null;
    }

    // auxiliares (arrumar explicação e deixá-los em ordem no fim)
    private boolean contemNumeros(String texto) {
        return texto.matches(".*\\d.*");
    }

    private boolean contemLetras(String texto) {
        return texto.matches(".*[a-zA-Zá-úÁ-Ú].*");
    }

    // Salva os dados no banco
    public void salvarCliente(String nome, String documento, LocalDate dataNascimento, boolean cooperado,
                              LocalDate validadeCaf, String codigoCaf){
        E_Cliente e_cliente = new E_Cliente();
        e_cliente.setName(nome);
        e_cliente.setCpf_cnpj(documento);
        e_cliente.setBirthDate(dataNascimento);
        e_cliente.setCooperated(cooperado);
        if (cooperado) {
            e_cliente.setMaturity_caf(validadeCaf);
            e_cliente.setCaf(codigoCaf);
        }
        r_cliente.save(e_cliente);
    }

}