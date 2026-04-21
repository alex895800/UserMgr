package it.alex89.usermgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
public class UsermgrApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsermgrApplication.class, args);
	}

}
