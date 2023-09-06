package com.intelizign.datamigration.payload;

import org.springframework.stereotype.Service;

@Service
public class PTCDocumentRecord {

	private String workItemId;
	private String section;
	private String category; 
	private String text ;
	private String fillingInstructions;
	private String additionalComments ;
	private String inRelease;
    private String state;
    
	public PTCDocumentRecord(String workItemId, String section, String category, String text,
			String fillingInstructions, String additionalComments, String inRelease, String state) {
		this.workItemId = workItemId;
		this.section = section;
		this.category = category;
		this.text = text;
		this.fillingInstructions = fillingInstructions;
		this.additionalComments = additionalComments;
		this.inRelease = inRelease;
		this.state = state;
	}


	public PTCDocumentRecord() {
		super();
	}


	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFillingInstructions() {
		return fillingInstructions;
	}

	public void setFillingInstructions(String fillingInstructions) {
		this.fillingInstructions = fillingInstructions;
	}

	public String getAdditionalComments() {
		return additionalComments;
	}

	public void setAdditionalComments(String additionalComments) {
		this.additionalComments = additionalComments;
	}

	public String getInRelease() {
		return inRelease;
	}

	public void setInRelease(String inRelease) {
		this.inRelease = inRelease;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
	
}
