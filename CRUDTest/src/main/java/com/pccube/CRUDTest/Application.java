package com.pccube.CRUDTest;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.pccube.CRUDTest.entities.User;
import com.pccube.CRUDTest.entities.UserRepository;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);

	}


	@Bean
	public CommandLineRunner demo(UserRepository repository) {
		return (args) -> {

			repository.save(
					new User("Paola", "$2a$10$YFkGNERY3/kiPn.5B1rZIuIzJ8TXzsIYE9qtWis0VtaVb9jMevzGe", "admin", null));
			repository.save(
					new User("Alfino", "$2a$10$pRp.r2trcVr7oswVeWvBx.XhuhnJF3DDeAQw/ePPMQDQ7NEI29nD6", "user", null));
			repository.save(
					new User("ciccio", "$2a$10$6Qwh/W/NU4W.8VPnMCAK1ukBRTXKEe00XOAJF9kFCnvu4TdD2wNlS", "user", null));

		};
	}

}