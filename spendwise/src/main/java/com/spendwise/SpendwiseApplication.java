package com.spendwise;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jakarta.persistence.EntityManager;

@SpringBootApplication
public class SpendwiseApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpendwiseApplication.class, args);
	}

	@Bean

CommandLineRunner runner(EntityManager em) {

return args -> {

List<?> tables = em.createNativeQuery("SHOW TABLES")

.getResultList();

System.out.println("===== TABLES =====");

tables.forEach(System.out::println);

};

}
}
