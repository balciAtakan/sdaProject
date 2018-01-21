package sda.web.backingbeans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class StrukturBean {
	
	public String logout(){
		//personenService.setCurrUser(null);	
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

}
