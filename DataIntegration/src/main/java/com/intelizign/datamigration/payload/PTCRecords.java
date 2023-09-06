package com.intelizign.datamigration.payload;

import org.springframework.stereotype.Service;

@Service
public class PTCRecords {
	private String workItemId;
	private String type;
	private String summary; 
	private String state ;
	private String priority;
	private String effort ;
	private String assignedUser;
    private String project;
    
	public PTCRecords(String workItemId, String type, String summary, String state, String priority, String effort,
			String assignedUser, String project) {
		this.workItemId = workItemId;
		this.type = type;
		this.summary = summary;
		this.state = state;
		this.priority = priority;
		this.effort = effort;
		this.assignedUser = assignedUser;
		this.project = project;
	}
    
	public PTCRecords() {
		super();
	}
	
	public String getWorkItemId() {
		return workItemId;
	}

	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getEffort() {
		return effort;
	}

	public void setEffort(String effort) {
		this.effort = effort;
	}

	public String getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

}
