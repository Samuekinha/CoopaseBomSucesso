package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Entities.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
public class S_CadastroCliente implements AutoCloseable {
    private final R_Cliente r_cliente;  // Adicionado final para injeção
    private final Scanner scanner;

    // Construtor recebe apenas R_Cliente (Spring injeta) e cria o Scanner internamente
    public S_CadastroCliente(R_Cliente r_cliente) {
        this.r_cliente = r_cliente;
        this.scanner = new Scanner(System.in); // Cria o Scanner aqui
    }

    // Constantes de configuração
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final String COMANDO_PARADA = "stop";
    private static final int TAMANHO_MAXIMO_NOME = 40;
    private static final int TAMANHO_MAXIMO_CAF = 40;
    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_CNPJ = 14;
    private static final int TAMANHO_DATA = 8;


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

            LocalDate validadeCaf = null; 
            if (cooperado) {
                if ((validadeCaf = solicitarValidadeCaf())== null) return "Cadastro cancelado";
            }

            String codigoCaf = "";
            if (cooperado) {
                if ((codigoCaf = solicitarCodigoCaf()) == null) return "Cadastro cancelado";
            }

            // ... salvar os dados

            salvarCliente(nome, documento, dataNascimento, cooperado, validadeCaf, codigoCaf);
            return "Cliente cadastrado!";
        }  catch (Exception e) {
            return "Erro durante o cadastro: " + e.getMessage();
        }
    }

    // auxiliares (arrumar explicação e deixá-los em ordem no fim)
    private static final Pattern NUMEROS_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern LETRAS_PATTERN = Pattern.compile(".*[a-zA-Zá-úÁ-Ú].*");

    private boolean contemNumeros(String texto) {
        return NUMEROS_PATTERN.matcher(texto).matches();
    }

    private boolean contemLetras(String texto) {
        return LETRAS_PATTERN.matcher(texto).matches();
    }

    private boolean validaDocumento(String input) {
        if (r_cliente.findByDocumento(input).isPresent()) {
            return true;
        }
        return false;
    }

    // Validações

    // Validação do nome
    private String solicitarNome() {
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

            if (input.length() > TAMANHO_MAXIMO_NOME) {
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
            if (input.length() != TAMANHO_CNPJ && input.length() != TAMANHO_CPF){
                System.out.println("Erro: O Cpf ou Cnpj podem ter apenas 11 ou 14 números. Tente novamente.");
                continue;
            }

            if (validaDocumento(input)) {
                System.out.println("Erro: O Cpf já está cadastrado na base de dados. Tente novamente.");
                continue;
            }

            return input;
        }
    }

    // Validação da data de nascimento
    private LocalDate solicitarDataNascimento() {
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
                if (input.length() != TAMANHO_DATA) {
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

    // Validação de cooperado
    private Boolean solicitarCooperado() {
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

            // Verifica input
            if (input.equalsIgnoreCase("sim")){
                System.out.println("Cooperado ativado com sucesso!");
                return true;
            }

            return false;
        }
    }

    // Validação da data de nascimento
    private LocalDate solicitarValidadeCaf() {
        while (true) {
            System.out.println("Digite a data de vencimento da CAF (ddMMyyyy) ou '" + COMANDO_PARADA +
                    "' para cancelar:");
            String input = scanner.nextLine().trim();

            // Verifica se input é nulo
            if (input.isEmpty()) {
                System.out.println("Erro: Data de vencimento da CAF não pode ser vazia. Tente novamente.");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            try {
                // Garante que data tenha padrão de caracteres
                if (input.length() != TAMANHO_DATA) {
                    throw new IllegalArgumentException("Data de vencimento deve ter exatamente 8 dígitos.");
                }

                // Transforma data (String) para data (LocalDate)
                LocalDate data = LocalDate.parse(input, FORMATO);

                // Garante que a data seja futura do momento de cadastro
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
    }

    // Validação do código de CAF
    private String solicitarCodigoCaf() {
        while (true) {
            System.out.println("Digite o código da CAF do cliente (ou '" + COMANDO_PARADA +
                    "' para cancelar):");
            String input = scanner.nextLine().trim();

            // Verifica se está vazio
            if (input.isEmpty()) {
                System.out.println("Erro: Código de CAF não pode ser vazio.");
                continue;
            }

            // Limita o tamanho do código CAF
            if (input.length() > TAMANHO_MAXIMO_CAF) {
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
    }

    // Salva os dados no banco
    public void salvarCliente(String nome, String documento, LocalDate dataNascimento, boolean cooperado,
                              LocalDate validadeCaf, String codigoCaf){
        try {
            E_Cliente e_cliente = new E_Cliente();
            e_cliente.setName(nome);
            e_cliente.setDocumento(documento);
            e_cliente.setBirthDate(dataNascimento);
            e_cliente.setCooperated(cooperado);
            if (cooperado) {
                e_cliente.setMaturity_caf(validadeCaf);
                e_cliente.setCaf(codigoCaf);
            }
            r_cliente.save(e_cliente);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar cliente: " + e.getMessage(), e);
        }
    }

    // ??
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
            System.out.println("Scanner fechado com AutoCloseable");
        }
    }

}