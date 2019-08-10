package main.java.sda.web.util;

public enum DfXSubCategory
{
    SubDfA("DfA Sub"), SubDfA2("DfA Sub2"), SubDfC("DfC Sub"), SubDfE("DfE Sub"), SubDfMa("DfMa Sub"), SubDfM(
        "DfM Sub"), SubDfS("DfS Sub"), SubDfR("DfR Sub"), SubDfQ("DfQ Sub");

    private String longText;

    DfXSubCategory(String longText)
    {
        this.longText = longText;
    }

    public String getLongText()
    {
        return longText;
    }

    public void setLongText(String longText)
    {
        this.longText = longText;
    }

    public static DfXSubCategory getEnum(String input, boolean fromText)
    {
        if (input == null) return null;
        else
        {
            for (DfXSubCategory category : DfXSubCategory.values())
            {
                if (!fromText)
                {
                    if (category.name().equalsIgnoreCase(input)) return category;
                } else if (category.getLongText().equalsIgnoreCase(input)) return category;
            }
            throw new IllegalArgumentException();
        }
    }
}