package main.java.sda.web.views;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KnowledgeRoomMessageView {

    private String uuid;

    private String message;
    private Date messageDate;
    private PersonView messageOwner;
    private String knowledgeRoomId;

    private boolean found;
    private String highlightedWord;

    private String prefixMessage;
    private String postfixMessage;

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

    public void copyView(KnowledgeRoomMessageView view) {
        this.uuid = view.getUuid();
        this.message = view.getMessage();
        this.messageDate = view.getMessageDate();
        this.messageOwner = view.getMessageOwner();
        this.knowledgeRoomId = view.getKnowledgeRoomId();
        this.found = view.isFound();
        this.highlightedWord = view.getHighlightedWord();
        this.prefixMessage = view.getPrefixMessage();
        this.postfixMessage = view.getPostfixMessage();
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

    public String getPrefixMessage() {
        return prefixMessage;
    }

    public void setPrefixMessage(String prefixMessage) {
        this.prefixMessage = prefixMessage;
    }

    public String getPostfixMessage() {
        return postfixMessage;
    }

    public void setPostfixMessage(String postfixMessage) {
        this.postfixMessage = postfixMessage;
    }

    public String getModifyDate(){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(messageDate);

        return format;
    }
}
