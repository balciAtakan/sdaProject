package main.java.sda.web.backingbeans;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeRoomService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.views.PersonView;

@Component
@Scope("view")
public class HomeBean {

	private static Logger log = LogManager.getLogger(HomeBean.class);

	@Autowired
	private PersonenService personenService;
	
	@Autowired
	private KnowledgeRoomService knowledgeRoomService;
	
	private PersonView currUser;
	
	private String usernameForDelete;
	private String roomnameForDelete;
	
	@PostConstruct
	public void init(){
		log.info("Home bean init!");
		
		setCurrUser(personenService.getCurrUser());
		
	}

	public String logout(){
		
		personenService.setCurrUser(null);	
		return "login?faces-redirect=true";
	}
	
	public boolean isRoleAdmin() {return currUser.getUsername().equals("abalci");}
	
	public String processDeleteUser() {
		
		if((usernameForDelete == null || usernameForDelete.isEmpty()) && (roomnameForDelete == null || roomnameForDelete.isEmpty()))
		{
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please enter a room or username for delete!",""));
			return null;
		}
		
		try {
			if(usernameForDelete != null && !usernameForDelete.isEmpty()){
				personenService.deleteUser(usernameForDelete);
				log.info("User is deleted!");
				FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_INFO,"User is deleted",""));
			}
			if(roomnameForDelete != null && !roomnameForDelete.isEmpty()){
				knowledgeRoomService.deleteKnowledgeRoom(roomnameForDelete);
				log.info("Room is deleted!");
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Room is deleted",""));
			}
			
		} catch (SDAException e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			return null;
		}
		
		return "home?faces-redirect=true";
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

	public String getRoomnameForDelete() {
		return roomnameForDelete;
	}

	public void setRoomnameForDelete(String roomnameForDelete) {
		this.roomnameForDelete = roomnameForDelete;
	}
	
}