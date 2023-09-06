package com.intelizign.datamigration.consumer;

import java.net.MalformedURLException;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.xml.rpc.ServiceException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelizign.datamigration.payload.PTCRecords;
import com.intelizign.datamigration.services.ConsumerLog;
import com.intelizign.datamigration.services.DocumentConsumerLog;
import com.intelizign.datamigration.services.PolarionDocumentService;
import com.intelizign.datamigration.services.PolarionServices;

@Service
public class KafkaConsumers {
	private ConsumerLog consumerLog;
	private DocumentConsumerLog documentConsumerLog;
	private ObjectMapper objectMapper;
	private AtomicBoolean shutdownFlag;
	private PolarionServices polarionService;
	private PolarionDocumentService polarionDocumentService;

	public KafkaConsumers(ConsumerLog consumerLog, ObjectMapper objectMapper, PolarionServices polarionService,
			PolarionDocumentService polarionDocumentService, DocumentConsumerLog documentConsumerLog)
			throws MalformedURLException, ServiceException {
		this.consumerLog = consumerLog;
		this.objectMapper = objectMapper;
		this.shutdownFlag = new AtomicBoolean(false);
		this.polarionService = polarionService;
		this.polarionDocumentService = polarionDocumentService;
		this.documentConsumerLog = documentConsumerLog;

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			shutdownFlag.set(true);
			consumerLog.writeLog("Shutting down Kafka consumer gracefully...");
		}));
	}

	@KafkaListener(topics = "Migrate-WorkItemObject-To-Polarion", groupId = "polarion-consumer")
	public void consumeTopic1(String receivedMessage) {
		try {
			PTCRecords ptcrecords = objectMapper.readValue(receivedMessage, PTCRecords.class);
			polarionService.sendMessage(ptcrecords);
		} catch (Exception e) {
			consumerLog.writeLog("Exception is" + e + "\n");
			e.printStackTrace();

		}
	}

	// If shutdown is in progress, stop processing new messages
	@KafkaListener(topics = "Migrate-DocumentObject-To-Polarion", groupId = "polarion-consumer")
	public void consumeTopic2(String receievedMessage) {
		if (shutdownFlag.get()) {
			documentConsumerLog.writeLog("Shutting down, not processing new messages.");
			return;
		}

		try {
			StringBuilder ptcHomePageContent = objectMapper.readValue(receievedMessage, StringBuilder.class);
			polarionDocumentService.sendMessage(ptcHomePageContent);

		} catch (Exception e) {
			documentConsumerLog.writeLog("Exception is" + e + "\n");
			e.printStackTrace();

		}
	}

	// Graceful shutdown handling
	public void shutdown() {

	}
}
