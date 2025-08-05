package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.h2.tools.Console;


@SpringBootApplication
public class Application {

	public static void main(String[] args) throws Exception {
		Console.main(args);
		SpringApplication.run(Application.class, args);
	}

}
