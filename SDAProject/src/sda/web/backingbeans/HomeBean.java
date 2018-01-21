package sda.web.backingbeans;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.services.PersonenService;
import sda.web.views.PersonView;

@Component
@Scope("request")
public class HomeBean {

	@Autowired
	private PersonenService personenService;
	
	private PersonView currUser;
	
	@PostConstruct
	public void init(){
		System.out.println("Home bean init!");
		
		setCurrUser(personenService.getCurrUser());
		
	}

	public String logout(){
		
		personenService.setCurrUser(null);	
		return "login";
	}
	
	public String processHome(){
		return "home";
	}
	public String processCommunication(){
		return "communication";
	}
	public String processCreateKnowledge(){
		return "create";
	}
	public String processSearchKnowledge(){
		return "search";
	}
	
	//yet to be implemented 
	public String processDeleteKnowledge(){
		return "delete";
	}
	
	
	public PersonView getCurrUser() {
		return currUser;
	}

	public void setCurrUser(PersonView currUser) {
		this.currUser = currUser;
	}
	
}
