package com.intelizign.datamigration.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.intelizign.datamigration.payload.PTCDocumentRecord;
import com.intelizign.datamigration.producer.KafkaProducers;

@Service
public class ReadDocumentDataFromExcel {

	private static final Logger logger = LoggerFactory.getLogger(ReadDocumentDataFromExcel.class);
	private final KafkaProducers producer;
	private List<PTCDocumentRecord> ptcDocumentRecordList = new ArrayList<>();
	private final DocumentProducerLog log;
	StringBuilder ptcHomePagecontent = new StringBuilder();

	public ReadDocumentDataFromExcel(KafkaProducers producer, DocumentProducerLog log) {
		this.producer = producer;
		this.log = log;
	}

	public void getAllDataFromExcel() {
		try {
			String excelFilePath = "D:\\ApacheKhafka\\DocumentInExcel.xls";
			try (InputStream inputStream = new FileInputStream(excelFilePath);
					Workbook workbook = new HSSFWorkbook(inputStream)) {
				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(0);
				DataFormatter dataFormatter = new DataFormatter();

				// Create a mapping between header names and column indexes
				Map<String, Integer> headerIndexMap = new HashMap<>();
				for (Cell cell : headerRow) {
					headerIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
				}

				// sheet.getLastRowNum(); - length of index

				for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);
					PTCDocumentRecord ptcDocumentRecord = new PTCDocumentRecord(
							getCellValue(currentRow, headerIndexMap, "ID", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Section", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Category", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Text", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Filling Instructions", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Additional Comments", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "In Release", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "State", dataFormatter));
					ptcDocumentRecordList.add(ptcDocumentRecord);
				}
			}

			// Process and send data to Kafka
			int i = 1;
			for (PTCDocumentRecord ptcDataRef : ptcDocumentRecordList) {

				log.writeLog("--Iterate Excel Data" + "Index is :" + i + "..." + "WorkItem Id is: "
						+ ptcDataRef.getWorkItemId() + "," + "Section: " + ptcDataRef.getSection() + "," + "Category: "
						+ ptcDataRef.getCategory() + "," + "Text: " + ptcDataRef.getText() + ","
						+ "Filling Instructions: " + ptcDataRef.getFillingInstructions() + "," + "Additional Comments: "
						+ ptcDataRef.getAdditionalComments() + "," + "In Release: " + ptcDataRef.getInRelease() + ","
						+ "State: " + ptcDataRef.getState() + "," + "\n");
				i++;
			}

			addPTCDataToObject();

			producer.sendDocumentObjectToKafkaBroker(ptcHomePagecontent);

		} catch (IOException e) {
			log.writeLog("---Error Message is" + e.getMessage());
		} catch (Exception e) {
			log.writeLog("---Error Message is" + e.getMessage() + "\n");
		}

	}

	private String getCellValue(Row row, Map<String, Integer> headerIndexMap, String headerName,
			DataFormatter dataFormatter) {
		Integer columnIndex = headerIndexMap.get(headerName);
		return columnIndex != null ? dataFormatter.formatCellValue(row.getCell(columnIndex)) : "";
	}

	public void addPTCDataToObject() {

		for (PTCDocumentRecord record : ptcDocumentRecordList)

		{

			String section = record.getSection();

			section = section.replace(".", "");

			int len = section.length();

			if (len == 1) {
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}

			} else if (len == 2) {
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}

			} else if (len == 3) {
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}
			} else if (len == 4) {
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}
			} else {
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}

			}
			 
			/*switch (len) {
			case 1:
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}

			case 2:
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}
			case 3:

				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}
			case 4:
				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");

				}
			case 5:

				len = len + 1;
				ptcHomePagecontent.append("<h" + len + ">").append(record.getText()).append("</h" + len + ">");
				if (record.getFillingInstructions() != "") {
					ptcHomePagecontent.append("<p>" + record.getFillingInstructions() + "</p>");
				}

			default:
				break;
			}*/

		}
		

	}
}
