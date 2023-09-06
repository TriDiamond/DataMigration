package com.intelizign.datamigration.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProducerLog {

	private static final Logger logger = LoggerFactory.getLogger(ProducerLog.class);
	private File logFile;

	private void createLogFile() {
		try {
			logFile = new File("D:\\ApacheKhafka\\kafka1\\producer.log");
			logFile.createNewFile();
		} catch (Exception e) {
			logger.error("Error creating the log file", e);
		}
	}

	public void writeLog(String message) {
		createLogFile();
		try (FileWriter fileWriter = new FileWriter(logFile, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

			bufferedWriter.write(message);
			bufferedWriter.newLine();

		} catch (Exception e) {
			logger.error("Error writing to the log file", e);
		}
	}

	public void writeLog(String message, Throwable throwable) {
		createLogFile();
		try (FileWriter fileWriter = new FileWriter(logFile, true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

			printWriter.println(message);
			throwable.printStackTrace(printWriter);
			printWriter.println();

		} catch (Exception e) {
			logger.error("Error writing to the log file", e);
		}
	}
}
