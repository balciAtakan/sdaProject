package sda.web.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import sda.web.exception.SDAException;
import sda.web.services.KnowledgeRoomService;
import sda.web.services.PersonenService;
import sda.web.services.SessionService;
import sda.web.util.UserRole;
import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.KnowledgeRoomView;
import sda.web.views.PersonView;

@Component
@Scope("view")
public class CommunicationBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonenService personenService;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private KnowledgeRoomService roomService;
	
	private PersonView currUser;
	private ArrayList<KnowledgeRoomView> rooms;
	
	private KnowledgeRoomView newRoom;
	private List<UserRole> roles;
	private List<UserRole> selectedRoles;
	/////////////////////////////////////
	//----------------------------------/
	//----------------------------------/
	//----------------------------------/
	/////////////////////////////////////
	private KnowledgeRoomView activeRoom;
	private String message;
	
	
	@PostConstruct
	public void init(){
		
		System.out.println("Communication bean init!");

		roles = new ArrayList<UserRole>();
		addRoom();
		
		try {
			//load currentUser from processor
			setCurrUser(personenService.getCurrentPersonDaten(personenService.getCurrUser().getUuid()));
			//load all rooms
			rooms = roomService.getKnowledgeRooms();
		} catch (SDAException e) {
			System.out.println(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error!","Something went wrong on Load Knowledge Rooms"));
		}
		
	}
	
	public void addRoom(){
		
		UserRole[] roles = UserRole.values();
		for (UserRole userRole : roles) {
			this.roles.add(userRole);
			System.out.println("name: "+userRole.name() + " role: "+userRole.role());
		
		}
		newRoom = new KnowledgeRoomView();
		
	}
	
	public String processCreateRoom() throws IOException
	{
		if(newRoom.getRoomname() != null && newRoom.getRoomname().isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please enter a room name!",""));
			return null;
		}
		System.out.println("create room");
		
		//new room initial 
		newRoom.setAllowedRoles(selectedRoles);
		newRoom.setRoomOwner(currUser.getUuid());
		newRoom.getUsers().add(currUser);
		
		selectedRoles.forEach(System.out::println);
		
		try {
			
			roomService.saveKnowledgeRoom(newRoom);
		} catch (SDAException e) {
			
			System.out.println(e.getMessage());
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unknown error!",""));
			return null;
		}
		
		FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"New room is created!",""));
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	    return null;
		
	}
	
	public void processEnterRoom(){
		
		// todo!!!!
		/*
		if(activeRoom.getAllowedRoles().stream().anyMatch(role -> role.name().equals(currUser.getRole())))
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('enterRoomDlg').show();");
		}
		else
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('deniedEnterRoom').show();");
		}
		*/
		
	}
	
	public void loadRoomData(ActionEvent event)
	{
		try 
		{
			activeRoom.setHistory(roomService.getKnowledgeRoomData(activeRoom.getUuid()));
		} catch (SDAException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
		
	}
	
	
	
	public void processMessage(ActionEvent event)
	{	
		try 
		{
		
			KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView(message,new Date(),currUser,activeRoom.getUuid()); 
			
			messageView.setFound(processWords(messageView));
			
			activeRoom.getHistory().add(messageView);
		
			roomService.saveKnowledgeRoomMessage(messageView);
			
			
		} catch (SDAException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	////////////////////////////////////////////////
	//											 //
	//											 //
	//				Wording Process				 //
	//											 //
	// 											 //
	///////////////////////////////////////////////
	public boolean processWords(KnowledgeRoomMessageView message) 
	{
		
		String[] word= message.getMessage().toLowerCase().trim().split("\\s+");
		List<String> list = Arrays.stream(word).collect(Collectors.toList());
		
		ArrayList<KnowledgeRoomMessageView> history = activeRoom.getHistory();
		
		boolean found = false;
		for (KnowledgeRoomMessageView hist : history) {
			
			String[] temp = hist.getMessage().toLowerCase().trim().split("\\s+");
			List<String> list2 = Arrays.stream(temp).collect(Collectors.toList());
			
			found = list.stream().anyMatch(new HashSet<>(list2)::contains);
			
			if(found)
				return found;
		}
		
		return found;
	}
	
	public int getTotalRooms(){
		return rooms == null ? Integer.valueOf(0): rooms.size();
	}

	public List<UserRole> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<UserRole> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}



	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
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

	public KnowledgeRoomView getActiveRoom() {
		return activeRoom;
	}

	public void setActiveRoom(KnowledgeRoomView activeRoom) {
		this.activeRoom = activeRoom;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
