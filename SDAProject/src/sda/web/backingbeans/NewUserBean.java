package sda.web.backingbeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.exception.SDAException;
import sda.web.services.PersonenService;
import sda.web.util.UserRole;
import sda.web.views.PersonView;

@Component
@Scope("view")
public class NewUserBean {

	@Autowired
	private PersonenService personenService;
	
	private PersonView newUser;
	//private TreeMap<String, String> roles;
	private List<UserRole> selectedRoles;
	
	@PostConstruct
	public void init(){
		
		newUser = new PersonView();
		System.out.println("NewUser bean init!");
	}
	
	public String processCreateUser()
	{
		System.out.println("count of chosen roles: " + selectedRoles.size());
		newUser.setRoles(selectedRoles);
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
	
	public String processBack(){
		return "login";
	}

	public PersonView getNewUser() {
		return newUser;
	}

	public void setNewUser(PersonView newUser) {
		this.newUser = newUser;
	}
	
	public UserRole[] getUserRoleList() {return UserRole.values();}

	public List<UserRole> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<UserRole> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}
	
}
