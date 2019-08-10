package main.java.sda.web.views;

public class DfxView
{

    private String uuid;
    private String categoryName;
    private String subCategory;

    public DfxView(String uuid, String categoryName, String subCategory)
    {
        super();
        this.uuid = uuid;
        this.categoryName = categoryName;
        this.subCategory = subCategory;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getSubCategory()
    {
        return subCategory;
    }

    public void setSubCategory(String subCategory)
    {
        this.subCategory = subCategory;
    }

}
