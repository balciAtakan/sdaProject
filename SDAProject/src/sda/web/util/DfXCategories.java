package sda.web.util;

public enum DfXCategories 
{
	DfC("Design for Costing"),
	DfM("Design for Manufacturability"), 
	DfA("Design for Assembly"),
	DfQ("Design for Quality"), 
	DfS("Design for serviceability"),
	DfR("Design for Reuse"), 
	DfE("Design for Environment"),
	DfMa("Design for Maintainability");

    private String category;

    DfXCategories(String category) {
        this.category = category;
    }

    public String category() {
        return category;
    }
}