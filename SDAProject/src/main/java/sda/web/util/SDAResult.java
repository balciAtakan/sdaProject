package main.java.sda.web.util;

public class SDAResult
{

    private String message;
    private boolean resultOK;

    public SDAResult(String message, boolean resultOK)
    {

        this.message = message;
        this.resultOK = resultOK;
    }

    public SDAResult(String message)
    {

        this.message = message;
    }

    public SDAResult()
    {

    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public boolean isResultOK()
    {
        return resultOK;
    }

    public void setResultOK(boolean resultOK)
    {
        this.resultOK = resultOK;
    }


}
