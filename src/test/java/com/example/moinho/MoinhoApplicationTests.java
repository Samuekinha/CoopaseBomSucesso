package com.example.moinho;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // Isso fará com que use as configurações de teste
class MoinhoApplicationTests {
	@Test
	void contextLoads() {
		// Teste vazio apenas para verificar se o contexto carrega
	}
}