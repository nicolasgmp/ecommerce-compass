package br.com.nicolas.ecommerce_compass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching
@SpringBootApplication
public class EcommerceCompassApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceCompassApplication.class, args);
	}

}
