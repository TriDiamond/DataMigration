package com.intelizign.datamigration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.intelizign.datamigration.properties.ReadConfigProperties;

@SpringBootApplication
public class DataIntegrationApplication {

	@Autowired
    private ReadConfigProperties properties;
	
	public static void main(String[] args) {
		SpringApplication.run(DataIntegrationApplication.class, args);
		System.out.println("****Executed 1*****");
	}

	
}
