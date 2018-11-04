package sda.web.views;

import java.util.List;

import sda.web.util.UserRole;

public class PersonView {
	
	private String uuid;
	private String firstname;
	private String lastname;
	private String password; 
	private String username;
	private List<UserRole> roles;
	
	public PersonView() {
	}
	
	public PersonView(String uuid) {
		this.uuid = uuid;
	}
	public PersonView(String uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}
	
	public PersonView(String uuid, String firstname, String lastname, String username, List<UserRole> roles) {
		super();
		this.uuid = uuid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.username = username;
		this.roles = roles;
	}

	public PersonView(PersonView person)
	{
		this.uuid = person.getUuid();
		this.firstname = person.getFirstname();
		this.lastname = person.getLastname();
		this.username = person.getUsername();
		this.roles = person.getRoles();
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

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
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
