package com.ugf360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class DbdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbdemoApplication.class, args);
	}

}
