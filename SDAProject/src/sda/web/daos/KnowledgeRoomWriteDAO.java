package sda.web.daos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.exception.SDAException;
import sda.web.util.SDAUtil;
import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.KnowledgeRoomView;
import sda.web.views.PersonView;

@Repository
public class KnowledgeRoomWriteDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public KnowledgeRoomView saveKnowledgeRoom(KnowledgeRoomView view) throws SDAException {
		// statt ? wird :uuid verwendet...
		
		view.setUuid(SDAUtil.GenerateUuid());
		
		String sql = "INSERT INTO KNOWLEDGE_ROOM(uuid,roomname,room_owner,date_create) VALUES(:uuid , :roomname , :roomOwner, :date_create)";

		Map<String, Object> params = new HashMap<>();
		params.put("uuid", view.getUuid());
		params.put("roomname", view.getRoomname());
		params.put("roomOwner", view.getRoomOwner());
		params.put("date_create", new Date());
		
		try 
		{
			template.update(sql, params);
			
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
	}

	public boolean saveKnowledgeRoomMessage(KnowledgeRoomMessageView view) throws SDAException {
		String sql = "INSERT INTO KNOWLEDGE_ROOM_MESSAGE(uuid,content,owner,date,knowledge_room) VALUES(:uuid , :content , :owner, :date,:kr)";

		Map<String, Object> params = new HashMap<>();
		params.put("uuid", SDAUtil.GenerateUuid());
		params.put("content", view.getMessage());
		params.put("owner", view.getMessageOwner().getUuid());
		params.put("date", view.getMessageDate());
		params.put("kr", view.getKnowledgeRoomId());
		
		try 
		{
			return template.update(sql, params) == 1 ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new SDAException(e.getMessage());
		}
	}
	
	public boolean insertKnowledgeRoomRoles(KnowledgeRoomView view) throws SDAException {
		
		String sql = "INSERT INTO KNOWLEDGE_ROOM_ROLE(id,role_code,knowledge_room_id) VALUES";
		
		for(int i = 0; i< view.getAllowedRoles().size() ; i++) {
			
			sql += " (:id"+i+",:role_code"+i+", :knowledge_room_id"+i+")";
			sql += (i == view.getAllowedRoles().size() - 1) ? ";" : ",";
		}

		Map<String, Object> params = new HashMap<>();
		
		for(int i = 0; i< view.getAllowedRoles().size() ; i++) {
			
			params.put("id"+i, SDAUtil.GenerateUuid());
			params.put("role_code"+i, view.getAllowedRoles().get(i).name().toLowerCase());
			params.put("knowledge_room_id"+i, view.getUuid());
		}
		
		try 
		{
			return template.update(sql, params) != 0 ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new SDAException(e.getMessage());
		}
	}
	
	public boolean saveKnowledgeRoomUser(KnowledgeRoomView room, PersonView person) throws SDAException {
		String sql = "INSERT INTO KNOWLEDGE_ROOM_USER(id,username,knowledge_room_id,person_id) VALUES(:id,:username,:knowledge_room_id,:person_id)";

		Map<String, Object> params = new HashMap<>();
		params.put("id", SDAUtil.GenerateUuid());
		params.put("username", person.getUsername());
		params.put("knowledge_room_id", room.getUuid());
		params.put("person_id", person.getUuid());
		
		try 
		{
			return template.update(sql, params) == 1 ;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new SDAException(e.getMessage());
		}
	}
}
