package main.java.sda.web.services;

import main.java.sda.web.daos.KnowledgeRoomReadDAO;
import main.java.sda.web.daos.KnowledgeRoomWriteDAO;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.SDAResult;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Scope("session")
public class KnowledgeRoomService {

	@Autowired
	private KnowledgeRoomReadDAO knowledgeRoomReadDAO;
	
	@Autowired
	private KnowledgeRoomWriteDAO knowledgeRoomWriteDAO;

	private KnowledgeRoomView currentRoom;
	
	public ArrayList<KnowledgeRoomView> getKnowledgeRooms() throws SDAException {
		
		return knowledgeRoomReadDAO.getKnowledgeRooms();
	}
	
	public SDAResult saveKnowledgeRoom(KnowledgeRoomView view) throws SDAException{
		
		SDAResult result = new SDAResult();
		try {
			
			view = knowledgeRoomWriteDAO.saveKnowledgeRoom(view);
			knowledgeRoomWriteDAO.insertKnowledgeRoomRoles(view);
			knowledgeRoomWriteDAO.saveKnowledgeRoomUser(view, view.getNewUser());
			
			result.setMessage("The KnowledgeRoom is successfully created!");
		} catch (Exception e) {

			result.setMessage("Error on KnowledgeRoom save!");
		}
		
		return result;
	}
	
	public ArrayList<KnowledgeRoomMessageView> getKnowledgeRoomData(String roomId) throws SDAException{
		
		return knowledgeRoomReadDAO.getKnowledgeRoomData(roomId);
	}
	
	public SDAResult saveKnowledgeRoomMessage(KnowledgeRoomMessageView view) throws SDAException{
		
		SDAResult result = new SDAResult();
		if(knowledgeRoomWriteDAO.saveKnowledgeRoomMessage(view))
			result.setMessage("Message is successfully saved!");
		else 
			result.setMessage("Error on message save!");
		
		return result;
	}
	
	public void deleteKnowledgeRoom(String roomname) throws SDAException{
		
		knowledgeRoomReadDAO.deleteKnowledgeRoom(roomname);
	}

	public KnowledgeRoomView getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(KnowledgeRoomView currentRoom) {
		this.currentRoom = currentRoom;
	}

	public void reset(){

		setCurrentRoom(null);
	}
}
