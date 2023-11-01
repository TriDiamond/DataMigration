package com.intelizign.datamigration;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DataIntegrationApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DataIntegrationApplication.class, args);
		System.out.println("****Executed 1*****");
	}

	
}
