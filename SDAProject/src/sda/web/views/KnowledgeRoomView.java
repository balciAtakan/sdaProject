package sda.web.views;

import java.util.ArrayList;
import java.util.List;

import sda.web.util.UserRole;

public class KnowledgeRoomView {

	private String uuid;
	private String roomname;
	private String roomOwner;
	private List<UserRole> allowedRoles;
	
	/** Here is saved only the id from the person not other attributes! 
	 */
	private ArrayList<PersonView> users;
	private ArrayList<KnowledgeRoomMessageView> history;
	
	public KnowledgeRoomView(){
		
		this.users = new ArrayList<PersonView>();
		this.history = new ArrayList<KnowledgeRoomMessageView>();
	}
	
	public KnowledgeRoomView(String roomOwner){
		
		this.roomOwner = roomOwner;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public List<UserRole> getAllowedRoles() {
		return allowedRoles;
	}
	public void setAllowedRoles(List<UserRole> allowedRoles) {
		this.allowedRoles = allowedRoles;
	}
	public ArrayList<PersonView> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<PersonView> users) {
		this.users = users;
	}
	public ArrayList<KnowledgeRoomMessageView> getHistory() {
		return history;
	}
	public void setHistory(ArrayList<KnowledgeRoomMessageView> history) {
		this.history = history;
	}

	public String getRoomOwner() {
		return roomOwner;
	}

	public void setRoomOwner(String roomOwner) {
		this.roomOwner = roomOwner;
	}
	
}
