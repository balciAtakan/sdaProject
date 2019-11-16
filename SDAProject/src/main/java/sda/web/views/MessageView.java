package main.java.sda.web.views;

import java.util.ArrayList;
import java.util.List;

public class MessageView
{
    private String word;
    private boolean foundInDB;
    private boolean foundInUsage;

    private List<WordView> synonyms;

    private String origin;

    public MessageView(String word)
    {
        this.word = word;
        this.synonyms = new ArrayList<>();
    }

    public void copyView(MessageView view)
    {
        this.word = view.getWord();
        this.foundInDB = view.isFoundInDB();
        this.foundInUsage = view.isFoundInUsage();
        this.synonyms = view.getSynonyms();
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public boolean isFoundInDB()
    {
        return foundInDB;
    }

    public void setFoundInDB(boolean foundInDB)
    {
        this.foundInDB = foundInDB;
    }

    public boolean isFoundInUsage()
    {
        return foundInUsage;
    }

    public void setFoundInUsage(boolean foundInUsage)
    {
        this.foundInUsage = foundInUsage;
    }

    public List<WordView> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<WordView> synonyms) {
        this.synonyms = synonyms;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
