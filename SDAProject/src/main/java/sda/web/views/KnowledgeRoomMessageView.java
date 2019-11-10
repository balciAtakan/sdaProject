package main.java.sda.web.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class KnowledgeRoomMessageView
{

    private String uuid;

    private String message;
    private List<MessageView> words;

    private Date messageDate;
    private PersonView messageOwner;
    private String knowledgeRoomId;

    private String highlightedWord;

    public KnowledgeRoomMessageView()
    {
    }

    public KnowledgeRoomMessageView(String uuid, String message)
    {
        this.uuid = uuid;
        this.message = message;
        if (message != null && !message.isEmpty())
            setWordsList(message);
    }

    public KnowledgeRoomMessageView(String uuid, String message, Date messageDate, PersonView messageOwner, String knowledgeRoomId)
    {
        if (message != null && !message.isEmpty())
            setWordsList(message);
        this.uuid = uuid;
        this.message = message;
        this.messageDate = messageDate;
        this.messageOwner = messageOwner;
        this.knowledgeRoomId = knowledgeRoomId;
    }

    private void setWordsList(String givenMessage)
    {
        List<String> li = Arrays.asList(givenMessage.split("\\s"));
        List<MessageView> words = new ArrayList<>();
        li.forEach(a -> words.add(new MessageView(a)));
        setWords(words);
    }

    public void copyView(KnowledgeRoomMessageView view)
    {
        this.uuid = view.getUuid();
        this.message = view.getMessage();
        this.messageDate = view.getMessageDate();
        this.messageOwner = view.getMessageOwner();
        this.knowledgeRoomId = view.getKnowledgeRoomId();
        this.highlightedWord = view.getHighlightedWord();
        this.words = view.getWords();
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Date getMessageDate()
    {
        return messageDate;
    }

    public void setMessageDate(Date messageDate)
    {
        this.messageDate = messageDate;
    }

    public PersonView getMessageOwner()
    {
        return messageOwner;
    }

    public String getOwnerName()
    {
        return messageOwner.getUsername();
    }

    public void setMessageOwner(PersonView messageOwner)
    {
        this.messageOwner = messageOwner;
    }

    public String getKnowledgeRoomId()
    {
        return knowledgeRoomId;
    }

    public void setKnowledgeRoomId(String knowledgeRoomId)
    {
        this.knowledgeRoomId = knowledgeRoomId;
    }

    public String getHighlightedWord()
    {
        return highlightedWord;
    }


    public List<MessageView> getWords()
    {
        return words;
    }

    public void setWords(List<MessageView> words)
    {
        this.words = words;
    }

    public String getModifyDate()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String format = formatter.format(messageDate);

        return format;
    }
}
