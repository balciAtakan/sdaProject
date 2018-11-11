package sda.web.backingbeans;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.services.PersonenService;
import sda.web.views.PersonView;

@Component
@Scope("view")
public class HomeBean {

	@Autowired
	private PersonenService personenService;
	
	private PersonView currUser;
	
	private String usernameForDelete;
	
	@PostConstruct
	public void init(){
		System.out.println("Home bean init!");
		
		setCurrUser(personenService.getCurrUser());
		
	}

	public String logout(){
		
		personenService.setCurrUser(null);	
		return "login?faces-redirect=true";
	}
	
	public boolean isRoleAdmin() {return currUser.getUsername().equals("abalci");}
	
	public String processDeleteUser() {
		
		System.out.println("User is gone!!");
		return "home?faces-redirect=true";
	}
	
	public String processHome(){
		return "home?faces-redirect=true";
	}
	public String processCommunication(){
		return "communication?faces-redirect=true";
	}
	public String processCreateKnowledge(){
		return "create?faces-redirect=true";
	}
	public String processSearchKnowledge(){
		return "search?faces-redirect=true";
	}
	
	//yet to be implemented 
	public String processDeleteKnowledge(){
		return "delete?faces-redirect=true";
	}
	
	
	public PersonView getCurrUser() {
		return currUser;
	}

	public void setCurrUser(PersonView currUser) {
		this.currUser = currUser;
	}

	public String getUsernameForDelete() {
		return usernameForDelete;
	}

	public void setUsernameForDelete(String usernameForDelete) {
		this.usernameForDelete = usernameForDelete;
	}
	
	
	
}
