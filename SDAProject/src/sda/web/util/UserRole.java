package sda.web.util;

public enum UserRole 
{
	PDM("Project Development Manager"),
	SDE("Senior Development Engineer"), 
	JDE("Junior Development Engineer"),
	DE("Design Engineer"), 
	ME("Material Engineer"),
	SPE("Senior Production Engineer"), 
	JPE("Junior Production Engineer"),
	SPM("Senior Production Manager"), 
	SAE("Senior Assembly Engineer"), 
	JAE("Junior Assembly Engineer"),
	QM("Quality Manager"),
	CSS("Customer Service Specialist"), 
	SM("Sales Manager"), 
	MRM("Market Research Manager"), 
	TSE("Technical Service Engineer");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }
    
    public static UserRole getEnum(String name) {
    	if(name == null)
    		return null;
    	else {
    		for(UserRole role : UserRole.values())
    		{
    			if(role.name().equalsIgnoreCase(name))
    				return role;
    		}
    		throw new IllegalArgumentException();
    	}
    }
}