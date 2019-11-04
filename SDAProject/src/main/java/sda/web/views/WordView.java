package main.java.sda.web.views;

import java.util.ArrayList;
import java.util.List;

public class WordView
{
    private String word;
    private int score;
    private List<WordView> synonyms;

    public WordView(String word, int score)
    {
        this.word = word;
        this.score = score;
        synonyms = new ArrayList<>();
    }

    public WordView(String word)
    {
        this.word = word;
        this.score = 0;
        synonyms = new ArrayList<>();
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public List<WordView> getSynonyms()
    {
        return synonyms;
    }

    public void setSynonyms(List<WordView> synonyms)
    {
        this.synonyms = synonyms;
    }
}
