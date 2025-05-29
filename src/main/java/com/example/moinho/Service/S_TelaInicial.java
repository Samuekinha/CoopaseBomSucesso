package com.example.moinho.Service;

import com.example.moinho.Service.S_Cliente.S_Cadastro;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class S_TelaInicial {

    private final Scanner scanner;
    private final S_Cadastro s_cliente;

    // Injeção das dependências
    public S_TelaInicial(S_Cadastro s_cliente) {
        this.scanner = new Scanner(System.in);
        this.s_cliente = s_cliente;
    }

    public String TelaInicial() {
        int choosedArea = AreaChooser();

        String[] areaNames = {"nulo", "Cliente", "Venda", "Despesa", "Cofre", "Funcionário"};

        System.out.println("Você está na área de " + areaNames[choosedArea] + " \n");
        director(serviceChooser(areaNames[choosedArea]), choosedArea);
        // direcionador recebe o serviço escolhido e a area escolhida

        return "a";
    } //what is that

    public int AreaChooser() {
        int choosedArea = 0;
        boolean choosed = false;

        while (!choosed) {
            System.out.println("Escolha uma das opções a seguir: ");
            System.out.println("1. Área cliente \n" +
                    "2. Área vendas \n" +
                    "3. Área despesas \n" +
                    "4. Área cofre \n" +
                    "5. Área funcionários");
            choosedArea = scanner.nextInt();

            if (choosedArea > 5 || choosedArea < 1) {
                System.out.println("Opção inválida, recomeçando...");
                break;
            } else {
                choosed = true;
            }
        }
        return choosedArea;
    } //lastttt

    public int serviceChooser(String areaName){
        int choosedService = 0;
        boolean choosed = false;

        while (!choosed) {
            System.out.println("Escolha uma das opçoes a seguir: ");
            System.out.println("1. Cadastrar um(a) " + areaName + " \n" +
                    "2. Editar um(a) " + areaName + " \n" +
                    "3. Excluir um(a) " + areaName + " \n" +
                    "4. Consultar o(a)s " + areaName + "s");
            choosedService = scanner.nextInt();

            if (choosedService > 4 || choosedService < 1) {
                System.out.println("Opção inválida, recomeçando...");
                break;
            } else {
                choosed = true;
            }
        }

        return choosedService;
    } //but im a creep

    public void director(int servico, int area){

        switch (area){
            case 1:
                switch (servico){
                    case 1: System.out.println(s_cliente.cadastrarCliente());
                    case 2: //s_cliente.editarCliente();
                    case 3: //1
                    case 4: //2
                }
                break;
            case 2:
                System.out.println("case 2");
                break;
        }

    } //end of mother monster

}
