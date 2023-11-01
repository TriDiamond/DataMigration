package com.intelizign.datamigration.services;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import org.apache.axis.description.TypeDesc;
import org.springframework.stereotype.Service;

import com.polarion.alm.ws.client.WebServiceFactory;
import com.polarion.alm.ws.client.session.SessionWebService;
import com.polarion.alm.ws.client.tracker.TrackerWebService;
import com.polarion.alm.ws.client.types.tracker.WorkItem;


@Service
public class CreateCustomRevision {
	
	private DocumentProducerLog documentLog;
	
	public CreateCustomRevision(DocumentProducerLog documentLog) {
		this.documentLog = documentLog;
	}

	public void getRevision() throws ServiceException, MalformedURLException, RemoteException {
		documentLog.writeLog("---Get Revision Method---\n");
		WebServiceFactory factory = new WebServiceFactory("http://denbg0415vm.izd01.in/polarion/ws/services/");
		SessionWebService sessionService = factory.getSessionService();
		sessionService.logIn("benish", "Bm08021998@");
		TrackerWebService trackerWebService = factory.getTrackerService();
		//String luceneQuery = "project.id:MbseIntegration AND id:50221 AND type:task";
		//String[] fields = { "status", "severity", "priority"};
		//Revision [] revision = trackerWebService.queryRevisions(luceneQuery, "id", fields);
		WorkItem workItemUri = trackerWebService.getWorkItemById("MbseIntegration", "50221");
		documentLog.writeLog("---The WorkItem Uri is---"+workItemUri.getUri()+"\n");
		documentLog.writeLog("---The workItem Metadata Object is ---"+WorkItem.getTypeDesc()+"\n");	
		TypeDesc metadataObject = WorkItem.getTypeDesc();
		String namespace = metadataObject.getXmlType().getNamespaceURI();
		QName qname = metadataObject.getXmlType();
		String getFieldNameForAttribute = metadataObject.getFieldNameForAttribute(qname);
		documentLog.writeLog("Get Namespace " + namespace +"Qname is"+qname + "Get Field Name For Attribute"+getFieldNameForAttribute+"\n" );
		
	}
}
