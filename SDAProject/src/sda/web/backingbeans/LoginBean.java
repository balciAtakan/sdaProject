package sda.web.backingbeans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.services.PersonenService;

@Component
@Scope("request")
public class LoginBean {

	private String username;
	private String password;

	@Autowired
	private PersonenService personenService;

	@PostConstruct
	public void init(){

		username=null;
		password=null;
		System.out.println("Login bean init!");
	}
	
	public String doNavigation()
	{
		System.out.println("navigation method");
		if(username != null && password != null)
		{
			if(personenService.checkCredentials(username, password))
				return "home";

			//developer shortcut ;)
			if(username.equals("admin")&& password.equals("admin"))
				return "home";
			else{ 
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error!","Username or password is wrong."));
				return null;
			}
		}
		else 
			return null;
	}
	
	public String processNewUser(){
		return "newUser";
	}
	
	
	
	@PreDestroy
	public void destroy(){

		username = null;
		password=null;
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
	public void setPersonenService(PersonenService personenService) {
		this.personenService = personenService;
	}

	public PersonenService getPersonenService() {
		return personenService;
	}

}
