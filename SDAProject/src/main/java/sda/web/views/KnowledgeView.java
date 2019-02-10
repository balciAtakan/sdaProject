package main.java.sda.web.views;

import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;

import java.util.Date;

public class KnowledgeView {

	private String uuid;
	private String word;
	private String knowledge_text;
	private DfXCategory dfXCategory;
	private DfXSubCategory dfXSubCategory;
	private Date modifyDate;
	private String ownerID;
	private String ownerUsername;
	private String filename;

	public KnowledgeView() {
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getKnowledge_text() {
		return knowledge_text;
	}

	public void setKnowledge_text(String knowledge_text) {
		this.knowledge_text = knowledge_text;
	}

	public DfXCategory getDfXCategory() {
		return dfXCategory;
	}

	public void setDfXCategory(DfXCategory dfXCategory) {
		this.dfXCategory = dfXCategory;
	}

	public DfXSubCategory getDfXSubCategory() {
		return dfXSubCategory;
	}

	public void setDfXSubCategory(DfXSubCategory dfXSubCategory) {
		this.dfXSubCategory = dfXSubCategory;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}
}
