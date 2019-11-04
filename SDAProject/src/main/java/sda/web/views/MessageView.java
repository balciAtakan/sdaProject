package main.java.sda.web.views;

public class MessageView
{
    private String word;
    private boolean foundInDB;
    private boolean foundInUsage;

    MessageView(String word)
    {
        this.word = word;
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
}
