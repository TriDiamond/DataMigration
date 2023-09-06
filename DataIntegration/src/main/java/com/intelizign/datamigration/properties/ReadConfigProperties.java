package com.intelizign.datamigration.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.intelizign.datamigration.services.ProducerLog;

@Service
public class ReadConfigProperties {
	
	private Properties properties ;
    private ProducerLog log ;
    private FileInputStream fileInputStream;
	
	public ReadConfigProperties(Properties properties, ProducerLog log) {
		this.properties = properties;
		this.log = log;
	}
  
	public void readPropertiesFile() {
        // Load properties from file
        
        try {
        try {
            fileInputStream = new FileInputStream("/DataIntegration/resources/dataconfig.properties");
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (IOException e) {
        	log.writeLog("Error Message is" + e.getMessage());
            log.writeLog("Exception is", e);
            return;
        }

        // Get property values
        String endpointUrl = properties.getProperty("endpoint");
        String destinationUsername = properties.getProperty("destination.username");
        String destinationPassword = properties.getProperty("destination.password");
        String sourceFilePath = properties.getProperty("sourceFilePath");

        // ... Use these values in your application logic

        log.writeLog("Endpoint URL: " + endpointUrl);
        log.writeLog("Destination Username: " + destinationUsername);
        log.writeLog("Destination Password: " + destinationPassword);
        log.writeLog("Source File Path: " + sourceFilePath);
        
        }catch(Exception e) {
        	log.writeLog("Error Message is" + e.getMessage());
        	log.writeLog("The Exception is", e);
        }
    }
        
}
