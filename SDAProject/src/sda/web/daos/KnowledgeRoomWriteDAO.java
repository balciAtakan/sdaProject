package sda.web.daos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.exception.SDAException;
import sda.web.util.SDAUtil;
import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.KnowledgeRoomView;

@Repository
public class KnowledgeRoomWriteDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public boolean saveKnowledgeRoom(KnowledgeRoomView view) throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "INSERT INTO KNOWLEDGE_ROOM(uuid,roomname,user_roles,users,room_owner) VALUES(:uuid , :roomname , :user_roles , :users, :roomOwner)";

		Map<String, Object> params = new HashMap<>();
		params.put("uuid", SDAUtil.GenerateUuid());
		params.put("roomname", view.getRoomname());
		params.put("user_roles", SDAUtil.buildUserRoles(view.getAllowedRoles()));
		params.put("users", SDAUtil.buildUserIds(view.getUsers()));
		params.put("roomOwner", view.getRoomOwner());
		
		try 
		{
			return template.update(sql, params) == 1 ;
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
}
