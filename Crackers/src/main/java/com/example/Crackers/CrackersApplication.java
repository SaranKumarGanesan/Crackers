package com.example.Crackers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrackersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrackersApplication.class, args);
		System.out.println(
				"\n============================================\t"
						+ "Spring server started successfully\t"
						+ "============================================\n\n"
		);
	}
}
