package com.example.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Controller / business siniflari 'modules', JPA entity'leri 'models',
// repository arayuzleri yine 'modules' altinda oldugu icin taranacak paketler acikca verilir.
@SpringBootApplication(scanBasePackages = { "com.example.backend", "modules" })
@EntityScan(basePackages = "models")
@EnableJpaRepositories(basePackages = "modules")
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
