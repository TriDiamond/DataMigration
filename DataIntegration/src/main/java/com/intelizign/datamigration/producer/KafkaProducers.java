package com.intelizign.datamigration.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelizign.datamigration.payload.PTCRecords;
import com.intelizign.datamigration.services.DocumentProducerLog;
import com.intelizign.datamigration.services.ProducerLog;

@Service
public class KafkaProducers {

	private KafkaTemplate<String, String> kafkaTemplate;
	private ProducerLog log;
	private ObjectMapper objectMapper;
	private DocumentProducerLog documentProducerLog;
	private int i = 0;
	private int j = 0;

	public KafkaProducers(KafkaTemplate<String, String> kafkaTemplate, ProducerLog log, 
			ObjectMapper objectMapper, DocumentProducerLog documentProducerLog) {
		this.kafkaTemplate = kafkaTemplate;
		this.log = log;
		this.objectMapper = objectMapper;
		this.documentProducerLog = documentProducerLog;
	}

	/*
	 * Send Workitem object to Kafka Broker
	 */
	public void sendWorkItemObjectToKafkaBroker(PTCRecords ptcRecord) {
		try {
             i++;
			String jsonPayload = objectMapper.writeValueAsString(ptcRecord);

			log.writeLog("---Passing json Object  and index is : "+"..."+ i + jsonPayload + "\n");

			kafkaTemplate.send("Migrate-WorkItemObject-To-Polarion", jsonPayload);

		} catch (Exception e) {
			log.writeLog("--Error Message is" + e.getMessage() + "\n");
			log.writeLog("Exception:", e);
		}
	}

	/*
	 * Send Document object to Kafka Broker
	 */
	public void sendDocumentObjectToKafkaBroker(StringBuilder ptcHomePageContent) {
		try {
			j++;
			String jsonPayload = objectMapper.writeValueAsString(ptcHomePageContent);

			documentProducerLog.writeLog("---Passing json Object  and index is : " + "..." + j + jsonPayload + "\n");

			kafkaTemplate.send("Migrate-DocumentObject-To-Polarion", jsonPayload);

		} catch (Exception e) {
			documentProducerLog.writeLog("--Error Message is" + e.getMessage() + "\n");
			documentProducerLog.writeLog("Exception:", e);
		}

	}
}
