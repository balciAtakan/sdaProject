package main.java.sda.web.util;

public enum DfXCategory
{
    DfA("Design for Assembly"), DfC("Design for Costing"), DfE("Design for Environment"), DfMa(
        "Design for Maintainability"), DfM("Design for Manufacturability"), DfS(
        "Design for Serviceability"), DfR("Design for Reuse"), DfQ("Design for Quality");

    private String longText;

    DfXCategory(String longText)
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

    public static DfXCategory getEnum(String input)
    {
        if (input == null) return null;
        else
        {
            for (DfXCategory category : DfXCategory.values())
            {
                if (category.name().equalsIgnoreCase(input)) return category;
            }
            throw new IllegalArgumentException();
        }
    }
}