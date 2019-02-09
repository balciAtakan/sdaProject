package main.java.sda.web.backingbeans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class StrukturBean {
	
	public String logout(){
		//personenService.setCurrUser(null);	
		return "login?faces-redirect=true";
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

}
