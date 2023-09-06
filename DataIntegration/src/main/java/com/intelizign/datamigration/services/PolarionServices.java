package com.intelizign.datamigration.services;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.intelizign.datamigration.payload.PTCRecords;
import com.polarion.alm.ws.client.WebServiceFactory;
import com.polarion.alm.ws.client.projects.ProjectWebService;
import com.polarion.alm.ws.client.session.SessionWebService;
import com.polarion.alm.ws.client.tracker.TrackerWebService;
import com.polarion.alm.ws.client.types.projects.Project;
import com.polarion.alm.ws.client.types.tracker.EnumOptionId;
import com.polarion.alm.ws.client.types.tracker.WorkItem;

@Service
public class PolarionServices {

	private static final Logger logger = LoggerFactory.getLogger(PolarionServices.class);
	private WebServiceFactory factory;
	
	//executorService is a thread pool to handle concurrent processing of records.
	private ExecutorService executorService;
	
	//recordQueue is a blocking queue that holds incoming PTCRecords objects before processing.
	private BlockingQueue<PTCRecords> recordQueue;
	
	//batchSize determines the number of records to process in a batch.
	private int batchSize = 100; 
	
	private int i = 1;

	private SessionWebService sessionService;
	private ConsumerLog consumerLog;

	public PolarionServices() throws MalformedURLException, ServiceException {
		this.factory = new WebServiceFactory("http://denbg0415vm.izd01.in/polarion/ws/services/");
		this.sessionService = factory.getSessionService();
		this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.recordQueue = new LinkedBlockingQueue<>();
		this.consumerLog = new ConsumerLog();
		startProcessingThread();
		try {
			login();
		} catch (Exception e) {
			consumerLog.writeLog("Error initializing PolarionServices", e);
		}
	}

	/*
	 * WorkItem created in current login user .If user has logout It login given
	 * credential and create WorkItem
	 */
	private void login() {
		try {
			sessionService.logIn("benish", "Bm08021998@");
		} catch (Exception e) {
			consumerLog.writeLog("Error logging in to Polarion", e);
		}
	}

	
	//The sendMessage method adds the received PTCRecords object to the recordQueue. 
	public void sendMessage(PTCRecords ptcRecords) {
		try {
			recordQueue.put(ptcRecords);
		} catch (InterruptedException e) {
			logger.error("Error adding record to the queue: " + e.getMessage(), e);
		}
	}
 
	/*The startProcessingThread method creates a new thread to 
	 * continuously process batches of records.
	 */
	private void startProcessingThread() {
		executorService.execute(() -> {
			while (true) {
				try {
					processBatch();
				} catch (Exception e) {
					logger.error("Error processing batch: " + e.getMessage(), e);
				}
			}
		});
	}

	/*
	 * The processBatch method takes records from the recordQueue in batches of batchSize and 
	 * calls the processRecord method to handle each record.
	 */
	private void processBatch() throws InterruptedException, RemoteException {
		int count = 0;
		while (count < batchSize) {
			PTCRecords record = recordQueue.take(); // Blocks until a record is available
			processRecord(record);
			count++;
		}
	}

	/*
	 * The processRecord method is a placeholder where you would implement the actual logic to 
	 * create work items based on the received records
	 */
	public void processRecord(PTCRecords ptcRecords) throws InterruptedException, RemoteException {

		try {

			sessionService.beginTransaction();
			ProjectWebService projectService = factory.getProjectService();
			TrackerWebService trackerService = factory.getTrackerService();
			Project project = projectService.getProject("MbseIntegration");
			
			//check the session 
            boolean session = sessionService.hasSubject();
			if(session) {
			if (project.getId() != null) {
				WorkItem wi = new WorkItem();
				wi.setProject(project);
				wi.setTitle(ptcRecords.getSummary() != null ? ptcRecords.getSummary() : null);
				wi.setId(ptcRecords.getWorkItemId());
				wi.setType(ptcRecords.getType() != null ? new EnumOptionId("task") : null);
				trackerService.createWorkItem(wi);

				consumerLog.writeLog("index is:.."+i+"  "+"Created work item Id is..." + wi.getId() + "\n");
				i++;
			} else {
				consumerLog.writeLog("Given Project Not in the Polarion server: " + project.getId());
			}
			}else {
				//If current session end again login the instance and executing
				consumerLog.writeLog("Session Ended : " + project.getId()+"\n");
				login();
			}
		} catch (Exception e) {
			consumerLog.writeLog("Error Message is: " + e.getMessage());
		} finally {
			sessionService.endTransaction(false);
			executorService.shutdown();
			
			
		}

	}
}
