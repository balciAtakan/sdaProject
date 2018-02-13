package sda.web.views;

import java.util.Date;

public class KnowledgeRoomMessageView {
	
	private String uuid;
	

	private String message;
	private Date messageDate;
	private PersonView messageOwner;
	private String knowledgeRoomId;
	
	public KnowledgeRoomMessageView(){
		
	}

	public KnowledgeRoomMessageView(String message, Date messageDate, PersonView messageOwner, String knowledgeRoomId) {
		super();
		this.message = message;
		this.messageDate = messageDate;
		this.messageOwner = messageOwner;
		this.knowledgeRoomId = knowledgeRoomId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}

	public PersonView getMessageOwner() {
		return messageOwner;
	}

	public void setMessageOwner(PersonView messageOwner) {
		this.messageOwner = messageOwner;
	}

	public String getKnowledgeRoomId() {
		return knowledgeRoomId;
	}

	public void setKnowledgeRoomId(String knowledgeRoomId) {
		this.knowledgeRoomId = knowledgeRoomId;
	}
	
	
}
