package sda.web.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.exception.SDAException;
import sda.web.services.KnowledgeRoomService;
import sda.web.services.PersonenService;
import sda.web.views.KnowledgeRoomView;
import sda.web.views.PersonView;

@Component
@Scope("request")
public class CommunicationBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	@Autowired
	private PersonenService personenService;
	
	@Autowired
	private KnowledgeRoomService roomService;
	
	private PersonView currUser;
	private ArrayList<KnowledgeRoomView> rooms;
	
	private KnowledgeRoomView newRoom;
	private List<String> roles;
	private String[] selectedRoles;
	
	@PostConstruct
	public void init(){
		
		System.out.println("Communication bean init!");

		roles = new ArrayList<String>();
		addRoom();
		//load currentUser from processor
		setCurrUser(personenService.getCurrUser());
		
		//load all rooms
		try {
			rooms = roomService.getKnowledgeRooms();
		} catch (SDAException e) {
			
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error!","Something went wrong on Load Knowledge Rooms"));
		}
		
	}
	
	public void addRoom(){
		
		roles.add("All");
		roles.add("Project Development Manager");
		roles.add("Senior Development Engineer");
		roles.add("Junior Development Engineer");
		roles.add("Design Engineer");
		roles.add("Material Engineer");
		roles.add("Senior Production Engineer");
		roles.add("Junior Production Engineer");
		roles.add("Senior Production Manager");
		roles.add("Senior Assembly Engineer");
		roles.add("Junior Assembly Engineer");
		roles.add("Quality Manager");
		roles.add("Customer Service Specialist");
		roles.add("Sales Manager");
		roles.add("Market Research Manager");
		roles.add("Technical Service Engineer");
		
	}
	
	public void processCreateRoom()
	{
		System.out.println("hi");
		for (String role : selectedRoles) {
			if(role.equals("All")){
				
				
			}
					
		}
	}
	
	public String[] getSelectedRoles() {
        return selectedRoles;
    }
 
    public void setSelectedRoles(String[] selectedRoles) {
        this.selectedRoles = selectedRoles;
    }
	
	public List<String> getRoles() {
		return roles;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PersonView getCurrUser() {
		return currUser;
	}

	public void setCurrUser(PersonView currUser) {
		this.currUser = currUser;
	}

	public ArrayList<KnowledgeRoomView> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<KnowledgeRoomView> rooms) {
		this.rooms = rooms;
	}

	public KnowledgeRoomView getNewRoom() {
		if(newRoom == null)
			return new KnowledgeRoomView();
		return newRoom;
	}

	public void setNewRoom(KnowledgeRoomView newRoom) {
		this.newRoom = newRoom;
	}
	
}
