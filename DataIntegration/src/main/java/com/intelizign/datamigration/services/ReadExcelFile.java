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
import org.springframework.stereotype.Service;

import com.intelizign.datamigration.payload.PTCRecords;
import com.intelizign.datamigration.producer.KafkaProducers;

@Service
public class ReadExcelFile {


	private final KafkaProducers producer;
	private final ProducerLog log;

	public ReadExcelFile(KafkaProducers producer, ProducerLog log) {
		this.producer = producer;
		this.log = log;
	}

	public void getAllDataFromExcel() {
		try {
			List<PTCRecords> ptcdataList = new ArrayList<>();
			String excelFilePath = "D:\\ApacheKhafka\\PTCWorkItem.xls";
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
				
				//sheet.getLastRowNum(); - length of index 

				for (int rowIndex = 1; rowIndex <=10; rowIndex++) {
					Row currentRow = sheet.getRow(rowIndex);

					PTCRecords ptcRecord = new PTCRecords(getCellValue(currentRow, headerIndexMap, "ID", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Type", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Summary", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "State", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Priority", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Effort", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Assigned User", dataFormatter),
							getCellValue(currentRow, headerIndexMap, "Project", dataFormatter));
					//ptcdataList.add(ptcRecord);
					producer.sendWorkItemObjectToKafkaBroker(ptcRecord);
				}
			}

			// Process and send data to Kafka
			/*int i = 1;
			for (PTCRecords ptcDataRef : ptcdataList) {

				log.writeLog("--Iterate Excel Data" + "Index is :" + i + "..." + "WorkItem Id is: "
						+ ptcDataRef.getWorkItemId() + "," + "type: " + ptcDataRef.getType() + "," + "summary: "
						+ ptcDataRef.getSummary() + "," + "state: " + ptcDataRef.getState() + "," + "priority: "
						+ ptcDataRef.getPriority() + "," + "effort: " + ptcDataRef.getEffort() + "," + "assignedUser: "
						+ ptcDataRef.getAssignedUser() + "," + "Project: " + ptcDataRef.getProject() + "," + "\n");
				i++;
			}*/

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
}
