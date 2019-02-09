package main.java.sda.web.views;

import java.util.Date;

public class KnowledgeRoomMessageView {

    private String uuid;

    private String message;
    private Date messageDate;
    private PersonView messageOwner;
    private String knowledgeRoomId;

    private boolean found;
    private String highlightedWord;

    public KnowledgeRoomMessageView() {

    }

    public KnowledgeRoomMessageView(String uuid,String message, Date messageDate, PersonView messageOwner, String knowledgeRoomId) {
        super();
        this.uuid = uuid;
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

    public String getOwnerName(){return messageOwner.getUsername();}

    public void setMessageOwner(PersonView messageOwner) {
        this.messageOwner = messageOwner;
    }

    public String getKnowledgeRoomId() {
        return knowledgeRoomId;
    }

    public void setKnowledgeRoomId(String knowledgeRoomId) {
        this.knowledgeRoomId = knowledgeRoomId;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getHighlightedWord() {
        return highlightedWord;
    }

    public void setHighlightedWord(String highlightedWord) {
        this.highlightedWord = highlightedWord;
    }
}
