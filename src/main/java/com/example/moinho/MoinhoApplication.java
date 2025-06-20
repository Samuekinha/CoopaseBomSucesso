package com.example.moinho;

import com.example.moinho.Service.S_Coisa;
import com.example.moinho.Service.S_TelaInicial;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoinhoApplication implements CommandLineRunner {

	private final S_TelaInicial telaInicial;

	public MoinhoApplication(S_TelaInicial telaInicial){
        this.telaInicial = telaInicial;
	}

	public static void main(String[] args) {
		SpringApplication.run(MoinhoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		telaInicial.TelaInicial();
	}
}
//
//	private final S_Coisa s_coisa;
//
//	public MoinhoApplication(S_Coisa s_coisa) {
//		this.s_coisa = s_coisa;
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.run(MoinhoApplication.class, args);
//	}
//
//	@Override
//	public void run(String... args) {
//		s_coisa.diabe();
//	}
//}

