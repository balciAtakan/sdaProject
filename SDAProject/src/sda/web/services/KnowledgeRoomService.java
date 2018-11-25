package sda.web.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import sda.web.daos.KnowledgeRoomReadDAO;
import sda.web.daos.KnowledgeRoomWriteDAO;
import sda.web.exception.SDAException;
import sda.web.util.SDAResult;
import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.KnowledgeRoomView;

@Service
@Scope("session")
public class KnowledgeRoomService {

	@Autowired
	private KnowledgeRoomReadDAO knowledgeRoomReadDAO;
	
	@Autowired
	private KnowledgeRoomWriteDAO knowledgeRoomWriteDAO;
	
	public ArrayList<KnowledgeRoomView> getKnowledgeRooms() throws SDAException{
		
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
}
