package com.intelizign.datamigration.controller;


import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.intelizign.datamigration.payload.User;
import com.intelizign.datamigration.services.CreateCustomRevision;
import com.intelizign.datamigration.services.DocumentProducerLog;
import com.intelizign.datamigration.services.ProducerLog;
import com.intelizign.datamigration.services.ReadDocumentDataFromExcel;
import com.intelizign.datamigration.services.ReadExcelFile;


@RestController
@RequestMapping("/api/v1/kafka")
public class MessageController {
	
	private ProducerLog log;
	private ReadExcelFile readExcelFile;
	private ReadDocumentDataFromExcel readDocumentData;
	private DocumentProducerLog documentLog;
	private CreateCustomRevision customRevision;
	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	public MessageController(ProducerLog log, ReadExcelFile readExcelFile, ReadDocumentDataFromExcel readDocumentData,
			DocumentProducerLog documentLog, CreateCustomRevision customRevision) {
		this.log = log;
		this.readExcelFile = readExcelFile;
		this.readDocumentData = readDocumentData;
		this.documentLog = documentLog;
		this. customRevision = customRevision;
	}

	/*
	 * http://localhost:8080/api/v1/kafka/migrateWorkItem
	 */
	@PostMapping("/migrateWorkItem")
	public ResponseEntity<String> triggerWorktemExcel(@RequestBody User user) {
		log.writeLog("---Migrate WorkItem API Triggered");
		//readExcelFile.getAllDataFromExcel();
		return ResponseEntity.ok("Sucessfully Message sent");
	}

	/*
	 * http://localhost:8080/api/v1/kafka/migrateDocument
	 */
	@PostMapping("/migrateDocument")
	public ResponseEntity<String> triggerDocumentExcel(@RequestBody User user) {
		documentLog.writeLog("---Migrate Document API Triggered**\n");
		//readDocumentData.getAllDataFromExcel();
		return ResponseEntity.ok("Sucessfully Message sent");
	}
	
	/*
	 * http://localhost:8080/api/v1/kafka/createCustomRevision
	 */
	@PostMapping("/createCustomRevision")
	public ResponseEntity<String> triggerObjectRevision(@RequestBody User user) throws MalformedURLException, RemoteException, ServiceException {
		documentLog.writeLog("---Migrate custom Revision API Triggered**\n");
		//customRevision.getRevision();
		return ResponseEntity.ok("Sucessfully Message sent");
	}
	
	/*
	 * http://localhost:8080/api/v1/kafka/convertSchemaToWsdl
	 */
	@PostMapping("/convertSchemaToWsdl")
	public ResponseEntity<String> convertSchemaToWsdl(@RequestBody User user) throws MalformedURLException, RemoteException, ServiceException {
		logger.info("Converting schema to wsdl Language");
		return ResponseEntity.ok("Sucessfully Message sent");
	}
}
