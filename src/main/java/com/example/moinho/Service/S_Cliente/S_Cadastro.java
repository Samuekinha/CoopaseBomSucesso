package com.example.moinho.Service.S_Cliente;

import com.example.moinho.Entities.E_Cliente;
import com.example.moinho.Repository.R_Cliente;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

@Service
public class S_Cadastro {
    private final R_Cliente r_cliente;  // Adicionado final para injeção
    private final Scanner scanner;

    public S_Cadastro(R_Cliente r_cliente, Scanner scanner) {
        this.r_cliente = r_cliente; //injeção via construtor
        this.scanner = scanner;
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

            boolean cooperado = solicitarCooperado();
            if (!cooperado) return "Cadastro cancelado";

            LocalDate validadeCaf = solicitarValidadeCaf();
            if (validadeCaf == null) return "Cadastro cancelado";

            String codigoCaf = solicitarCodigoCaf();
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
            String nome = scanner.nextLine().trim();

            // Verifica se está vazio
            if (nome.isEmpty()) {
                System.out.println("Erro: Nome não pode ser vazio.");
                continue;
            }

            // Verifica comando de parada
            if (nome.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            // Validação do nome
            if (contemNumeros(nome)) {
                System.out.println("Erro: Nomes não podem conter números. Tente novamente.");
                continue;
            }

            return nome;
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
                System.out.println("Erro: Cpf ou Cnpj não podem ser vazios.");
                continue;
            }

            // Verifica comando de parada
            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                System.out.println("Cadastro cancelado pelo usuário.");
                return null;
            }

            // Validação do nome
            if (contemLetras(input)) {
                System.out.println("Erro: Cpf ou Cnpj não podem conter letras. Tente novamente.");
                continue;
            }

            if (input.length() != 14 || input.length() != 11){
                System.out.println("Erro: O Cpf e Cnpj apenas podem ter 11 ou 14 números.");
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
                continue;
            }

            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                return null;
            }

            try {
                if (input.length() != 8) {
                    throw new IllegalArgumentException("Data deve ter exatamente 8 dígitos");
                }

                LocalDate data = LocalDate.parse(input, FORMATO);

                if (data.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Data não pode ser futura");
                }

                return data;

            } catch (DateTimeParseException e) {
                System.out.println("Erro: Formato de data inválido. Use ddMMyyyy.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    // auxiliares (arrumar explicação e deixá-los em ordem no fim)
    private boolean contemNumeros(String texto) {
        return texto.matches(".*\\d.*");
    }

    private boolean contemLetras(String texto) {
        return texto.matches(".*[a-zA-Zá-úÁ-Ú].*");
    }






    // Garantir que se pode injetar em um E_Cliente
    private static boolean podeCadastrar = true;
    // Nome e cpf/cnpj
    private String name = "", identification = "";
    // Se é ou não cooperado
    private static boolean coop = false;
    // Data nascimento
    private LocalDate localBornDate = null;
    // Vencimento da DAP
    private static LocalDate localMaturityCAF = null;
    // Código da CAF
    private static String CAF = "";

    public String cadastrarCliente() {

            // Nome


            // CPF/CNPJ
            System.out.println("CNPJ ou CPF do cliente (type stop)");
            while (true) {
                identification = scanner.nextLine().trim();
                if (verifyIfHasCharacter(identification)){
                    System.out.println("Identification has lyrics, type again or 'stop' to end");
                } else {
                    if (stopper(identification)) {
                        System.out.println("Aborting process...");
                        break;
                    }
                }
            }

            // Data de nascimento
        System.out.println("DIA, MÊS e ANO de nascimento do cliente (ddMMyyyy) (type stop)");

        try {
                String inputBirth = scanner.nextLine().trim(); // Usa nextLine() em vez de next()

                if (inputBirth.equalsIgnoreCase("stop")) {
                    System.out.println("Aborting process...");
                }

                // Usando DateTimeFormatter (Java 8+)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                localBornDate = LocalDate.parse(inputBirth, formatter);
                System.out.println("data válida");
            } catch (DateTimeParseException e) {
                return "Erro: Data inválida. Use o formato ddMMyyyy (ex: 29012008).";
            } catch (Exception e) {
                return "Erro inesperado: " + e.getMessage();
            }

            // Cooperado
            System.out.println("É cooperado? if yes 'y'. Type 'stop' to end");
            String inputCoop = scanner.nextLine().trim();
            if (inputCoop.equalsIgnoreCase("stop")) {
                System.out.println("Aborting process...");
            }
            if (inputCoop.equalsIgnoreCase("s")){
                coop = true;
                System.out.println("É cooperado!");
            }


            cooperado();

            // Se chegou aqui, cadastra o cliente
            injecaoDeDados();

        return "Cadastramento encerrado;";
    }

    // Metodo para informações cooperadas
    public String cooperado() {
        if (coop) {
            try {
                System.out.println("DIA, MÊS e ANO de vencimento da CAF (ddMMyyyy)");
                String inputCAF = scanner.nextLine().trim();

                if (inputCAF.equalsIgnoreCase("stop")) {
                    return "Cadastro cancelado.";
                }

                // Usando DateTimeFormatter (Java 8+)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
                localMaturityCAF = LocalDate.parse(inputCAF, formatter);
                System.out.println("data válida");

            } catch (DateTimeParseException e) {

                return "Erro: Data inválida. Use o formato ddMMyyyy (ex: 29012008).";
            } catch (Exception e) {

                return "Erro inesperado: " + e.getMessage();
            }

            // Código da CAF
            System.out.println("Qual o número/código da DAP/CAF? (n para cancelar)");

            if (CAF.equalsIgnoreCase("n")){
                return null;
            } else {
                CAF = scanner.nextLine().trim();
            }
        }
        return null;
    }

    //
    public void salvarCliente(String nome, String documento, LocalDate dataNascimento, boolean cooperado,
                              LocalDate validadeCaf, String codigoCaf){
        E_Cliente e_cliente = new E_Cliente();
        e_cliente.setName(nome);
        e_cliente.setCpf_cnpj(documento);
        e_cliente.setBirthDate(dataNascimento);
        e_cliente.setCooperated(cooperado);
        e_cliente.setMaturity_caf(validadeCaf);
        e_cliente.setCaf(codigoCaf);
    }

    public boolean verifyIfHasNumber(String input) {
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Se algum caractere for dígito (número), retorna falso.
            }
        }
        return false; // Se passou por todos, é válido.
    }

    public boolean verifyIfHasCharacter(String input) {
        for (char c : input.toCharArray()) {
            if (!Character.isDigit(c)) {
                return true; // Se algum caractere não for dígito (número), retorna falso.
            }
        }
        return false; // Se passou por todos, é válido.
    }

    public boolean stopper(String input){
        return input.equalsIgnoreCase("stop");
    }

}