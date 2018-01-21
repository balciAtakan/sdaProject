package sda.web.views;

import java.util.ArrayList;

import sda.web.util.SDAUtil;
import sda.web.util.UserRole;

public class KnowledgeRoomView {

	private String uuid;
	private String roomname;
	private ArrayList<UserRole> allowedRoles;
	private ArrayList<PersonView> users;
	private KnowledgeRoomHistoryView history;
	
	
	public KnowledgeRoomView(String uuid, String roomname, ArrayList<UserRole> allowedRoles, ArrayList<PersonView> users,
			KnowledgeRoomHistoryView history) {
		
		this.uuid = uuid;
		this.roomname = roomname;
		this.allowedRoles = allowedRoles;
		this.users = users;
		this.history = history;
	}
	
	public KnowledgeRoomView(String roomname, ArrayList<UserRole> allowedRoles){
		
		this.roomname = roomname;
		this.allowedRoles = allowedRoles;
		this.uuid = SDAUtil.GenerateUuid();
	}
	
	public KnowledgeRoomView(){
		
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
	public ArrayList<UserRole> getAllowedRoles() {
		return allowedRoles;
	}
	public void setAllowedRoles(ArrayList<UserRole> allowedRoles) {
		this.allowedRoles = allowedRoles;
	}
	public ArrayList<PersonView> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<PersonView> users) {
		this.users = users;
	}
	public KnowledgeRoomHistoryView getHistory() {
		return history;
	}
	public void setHistory(KnowledgeRoomHistoryView history) {
		this.history = history;
	}
	
}
