package Hallodos;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import Hallodos.controller.UserController;

@SpringBootApplication

public class HallodosApplication {

	public static void main(String[] args) {

		SpringApplication.run(HallodosApplication.class, args);
	}

}
