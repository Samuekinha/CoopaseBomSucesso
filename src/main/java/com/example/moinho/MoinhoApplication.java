package com.example.moinho;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoinhoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MoinhoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Código que será executado quando a aplicação iniciar
		System.out.println("Aplicação Moinho iniciada com sucesso!");

		// Aqui você pode adicionar lógica de inicialização
		// como carregar dados iniciais no banco, etc.
	}
}