package sda.web.backingbeans;

import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.exception.SDAException;
import sda.web.services.PersonenService;
import sda.web.views.PersonView;

@Component
@Scope("request")
public class NewUserBean {

	@Autowired
	private PersonenService personenService;
	
	private PersonView newUser;
	private TreeMap<String, String> roles;
	private String selectedrole;
	
	@PostConstruct
	public void init(){
		
		newUser = new PersonView();
		System.out.println("NewUser bean init!");
		
		roles = new TreeMap<String,String>();

		roles.put("Project Development Manager","PDM");
		roles.put("Senior Development Engineer","SDE");
		roles.put("Junior Development Engineer","JDE");
		roles.put("Design Engineer","DE");
		roles.put("Material Engineer","ME");
		roles.put("Senior Production Engineer","SPE");
		roles.put("Junior Production Engineer","JPE");
		roles.put("Senior Production Manager","SPM");
		roles.put("Senior Assembly Engineer","SAE");
		roles.put("Junior Assembly Engineer","JAE");
		roles.put("Quality Manager","QM");
		roles.put("Customer Service Specialist","CSS");
		roles.put("Sales Manager","SM");
		roles.put("Market Research Manager","MRM");
		roles.put("Technical Service Engineer","TSE");
	
	}
	
	public String processBack(){
		return "login";
	}

	public PersonView getNewUser() {
		return newUser;
	}

	public void setNewUser(PersonView newUser) {
		this.newUser = newUser;
	}

	public TreeMap<String, String> getRoles() {
		return roles;
	}

	public void setRoles(TreeMap<String, String> roles) {
		this.roles = roles;
	}

	public String getSelectedrole() {
		return selectedrole;
	}

	public void setSelectedrole(String selectedrole) {
		this.selectedrole = selectedrole;
	}
	public String processCreateUser()
	{
		System.out.println(selectedrole);
		newUser.setRole(selectedrole);
		try {
			personenService.createUser(newUser);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Success!","User is succesfully saved!"));
			return "login";
		} catch (SDAException e) {
		
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error!",e.getMessage()));
		}
		
		
		return null;
	}
	public String processCancel(){
		return "login";
	}
	
	
}
