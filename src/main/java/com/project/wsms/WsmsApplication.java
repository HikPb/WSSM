package com.project.wsms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication()
@EnableJpaAuditing
public class WsmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsmsApplication.class, args);
	}

}
