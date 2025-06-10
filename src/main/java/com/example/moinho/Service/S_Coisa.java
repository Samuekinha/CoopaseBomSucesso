package com.example.moinho.Service;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class S_Coisa {
    private final Scanner scanner;

    public S_Coisa() {
        this.scanner = new Scanner(System.in);
    }

    public String diabe() {
        double[] qtde = {
                7,
                35,
                36,
                12,
                23,
                18,
                6,
                6,
                26,
                26,
                2,
                31,
                31,
                17,
                9,
                13,
                7,
                8,
                6,
                14
        };
        String[] nomes = {
                "CRAS Bom Sucesso do Sul",
                "CRAS Clevelândia",
                "CRAS Coronel Vivida",
                "CRAS Honório Serpa",
                "CRAS Itapejara D'Oeste",
                "CRAS Mangueirinha",
                "Caritas Diocesana de Palmas",
                "Comunidade Terapêutica Novo Horizonte",
                "CRAS Centro - Palmas",
                "CRAS Lagoão - Palmas",
                "Casa de Passagem - Pato Branco",
                "CRAS Carolina Ferrari Amadori",
                "CRAS Paulina Bonalume Andreatta",
                "FUNDABEM - Fundação Patobranquense Do Bem Estar",
                "GAMA - Associação dos Amigos de Prevenção do Cancer",
                "ISSAL - INSTITUTO DE SAUDE SAO LUCAS DE PATO BRANCO",
                "Lar de Idosos São Francisco de Assis",
                "Missão SOS Vida Nova",
                "Remanso da Pedreira",
                "CRAS Vitorino"
        };

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nomes.length; i++) {
            String mensagem = "Nós da Coopase (Cooperativa Agroindustrial Santo Expedito) queremos confirmar com a " +
                    "sua entidade (" + nomes[i] + ") a periodicidade das entregas de farinha de " +
                    "fubá referente ao programa do Compra Direta Paraná. " +  // Substitui \n por espaço
                    "Proponhamos que sejam entregues a cada 3 meses " + qtde[i] * 3 + " kg de farinha de fubá moída na pedra (Sendo " + qtde[i] + " kg por mês). " +
                    "Meses de referência das 1°ra entrega: Junho, Julho e Agosto. " +
                    "Pedimos apenas que seja confirmado caso sua entidade esteja de acordo, nos respondendo com um 'Ok' ou 'De acordo'. " +
                    "As entregas estão iniciando nesta semana, após retorno de vossa parte. Se houverem dúvidas ou quaisquer " +
                    "outras questões a tratar, podem nos enviar e vamos responder o mais rápido possível!";

            // Adiciona a mensagem (sem quebras) ao StringBuilder
            sb.append(mensagem).append("\n"); // \n apenas como separador entre mensagens
        }

        // Imprime tudo de uma vez (opcional)
        System.out.print(sb.toString());
        return sb.toString();
    }
}