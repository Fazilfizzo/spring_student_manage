package com.fazil.learn_spring.learnspring_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LearnspringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearnspringJpaApplication.class, args);
	}

}
