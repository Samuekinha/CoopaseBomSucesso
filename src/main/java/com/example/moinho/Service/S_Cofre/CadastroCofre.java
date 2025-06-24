package com.example.moinho.Service.S_Cofre;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

@Service
public class CadastroCofre implements AutoCloseable {
    private final Scanner scanner;

    // Construtor cria o Scanner internamente
    public CadastroCofre() {
        this.scanner = new Scanner(System.in); // Cria o Scanner aqui
    }

    // Constantes de configuração
    private static final String COMANDO_PARADA = "stop";
    private static final String COMANDO_VOLTA = "return";
    private static final int TAMANHO_MAXIMO_NOME_COFRE = 40;

    public String CadastrarCofre(){
        try {
            String nome_cofre = solicitarNomeCofre();
            if (nome_cofre == null) return "Cadastro cancelado.";

        } catch (Exception e) {
            return "Erro desconhecido: " + e.getMessage();
        }

        return "";
    }

    // Validações

    public String solicitarNomeCofre() {
        while (true) {
            System.out.println("Digite o nome do cofre (ou '" + COMANDO_PARADA + "' para cancelar):");
            String input = scanner.nextLine().trim();

            if (input.length() > 25 || input.isEmpty()) {
                System.out.println("O nome precisa ter entre 1 e 25 caracteres! Tente novamente.");
                continue;
            }

            if (input.equalsIgnoreCase(COMANDO_VOLTA)) {
                System.out.println("Você está retomando a validação de nome de cofre.");
                continue;
            }

            if (input.equalsIgnoreCase(COMANDO_PARADA)) {
                return null;
            }

            return input;
        }
    }

    // Close scanner
    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
            System.out.println("Scanner fechado com AutoCloseable");
        }
    }

}
