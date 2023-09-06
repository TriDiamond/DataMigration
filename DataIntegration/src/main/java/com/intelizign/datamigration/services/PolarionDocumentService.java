package com.intelizign.datamigration.services;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import com.polarion.alm.ws.client.types.tracker.Module;

import javax.xml.rpc.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.polarion.alm.ws.client.WebServiceFactory;
import com.polarion.alm.ws.client.projects.ProjectWebService;
import com.polarion.alm.ws.client.session.SessionWebService;
import com.polarion.alm.ws.client.tracker.TrackerWebService;
import com.polarion.alm.ws.client.types.Text;
import com.polarion.alm.ws.client.types.projects.Project;
import com.polarion.alm.ws.client.types.tracker.EnumOptionId;

@Service
public class PolarionDocumentService {

	private static final Logger logger = LoggerFactory.getLogger(PolarionServices.class);
	private WebServiceFactory factory;

	// executorService is a thread pool to handle concurrent processing of records.
	private ExecutorService executorService;

	// recordQueue is a blocking queue that holds incoming PTCRecords objects before
	// processing.
	private BlockingQueue<StringBuilder> recordQueue;

	// batchSize determines the number of records to process in a batch.
	private int batchSize = 100;

	private int i = 1;

	private SessionWebService sessionService;
	private DocumentConsumerLog documentConsumerLog;

	public PolarionDocumentService() throws MalformedURLException, ServiceException {
		this.factory = new WebServiceFactory("http://denbg0415vm.izd01.in/polarion/ws/services/");
		this.sessionService = factory.getSessionService();
		this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		this.recordQueue = new LinkedBlockingQueue<>();
		this.documentConsumerLog = new DocumentConsumerLog();
		startProcessingThread();
		try {
			login();
		} catch (Exception e) {
			documentConsumerLog.writeLog("Error initializing PolarionServices", e);
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
			documentConsumerLog.writeLog("Error logging in to Polarion", e);
		}
	}

	// The sendMessage method adds the received PTCRecords object to the
	// recordQueue.
	public void sendMessage(StringBuilder ptcHomePageContent) {
		try {
			recordQueue.put(ptcHomePageContent);
		} catch (InterruptedException e) {
			logger.error("Error adding record to the queue: " + e.getMessage(), e);
		}
	}

	/*
	 * The startProcessingThread method creates a new thread to continuously process
	 * batches of records.
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
	 * The processBatch method takes records from the recordQueue in batches of
	 * batchSize and calls the processRecord method to handle each record.
	 */
	private void processBatch() throws InterruptedException, RemoteException {
		int count = 0;
		while (count < batchSize) {
			StringBuilder record = recordQueue.take(); // Blocks until a record is available
			processRecord(record);
			count++;
		}
	}

	public void processRecord(StringBuilder ptcHomePageContent) throws InterruptedException, RemoteException {

		try {

			
			ProjectWebService projectService = factory.getProjectService();
			TrackerWebService trackerService = factory.getTrackerService();
			Project project = projectService.getProject("MbseIntegration");
			String moduleTitle = "Imported_PTC-Document-Migration";

			ptcHomePageContent.insert(0, "<h1> " + moduleTitle + " </h1>");
			Text homePageContent = convertStringObjectToText(ptcHomePageContent.toString());

			// Set Allowed Work Item Types list
			EnumOptionId[] allowedWITypes = { new EnumOptionId("requirement"), new EnumOptionId("task") };

			// check the session
			boolean session = sessionService.hasSubject();
			if (session) {
				if (project.getId() != null) {
					String sDocumentUri = trackerService.createModule(project.getId(), "_default",
							moduleTitle, allowedWITypes, new EnumOptionId("duplicates"), false,
							null);
					Module doc = trackerService.getModuleByUri(sDocumentUri);

					doc.setType(new EnumOptionId("generic"));
					doc.setHomePageContent(homePageContent);
					doc.setUsesOutlineNumbering(true);
					trackerService.updateModule(doc);
					documentConsumerLog.writeLog("Created Document Id is" + doc.getId() + "\n");

				} else {
					documentConsumerLog.writeLog("Given Project Not in the Polarion server: " + project.getId());
				}

			} else {
				// If current session end again login the instance and executing
				documentConsumerLog.writeLog("Session Ended : " + project.getId() + "\n");
				login();
			}

		} catch (Exception e) {
			documentConsumerLog.writeLog("Error Message is: " + e.getMessage());
		} finally {
			
			executorService.shutdown();

		}

	}

	/*
	 * Convert String Obect To Text Object For passing Text Parameter In WorkItem
	 * Description Method
	 **/
	public Text convertStringObjectToText(String homePageContent) {
		Text homePagecontent = new Text();
		homePagecontent.setType("text/html");
		homePagecontent.setContent(homePageContent);
		homePagecontent.setContentLossy(false);
		return homePagecontent;

	}

}
