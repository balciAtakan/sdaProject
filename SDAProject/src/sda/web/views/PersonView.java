package sda.web.views;

public class PersonView {
	
	private String uuid;
	private String firstname;
	private String lastname;
	private String password; 
	private String username;
	private String role;
	
	public PersonView() {
	}
	
	public PersonView(String uuid, String firstname, String lastname) {
		this.uuid = uuid;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	public PersonView(PersonView person)
	{
		this.uuid = person.getUuid();
		this.firstname = person.getFirstname();
		this.lastname = person.getLastname();
		this.username = person.getUsername();
		this.role = person.getRole();
	}
	
	@Override
	public String toString() {
		return "PersonView [uuid=" + uuid + ", vorname=" + firstname + ", nachname=" + lastname + "]";
	}

	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
