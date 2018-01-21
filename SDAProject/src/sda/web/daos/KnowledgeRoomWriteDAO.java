package sda.web.daos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.exception.SDAException;
import sda.web.views.KnowledgeRoomView;

@Repository
public class KnowledgeRoomWriteDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public boolean saveKnowledgeRoom(KnowledgeRoomView view) throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "INSERT INTO KNOWLEDGE_ROOM VALUES(:uuid , :rooomname , :user_roles , :users )";

		Map<String, Object> params = new HashMap<>();
		params.put("uuid", view.getUuid());
		params.put("roomname", view.getRoomname());
		params.put("user_roles", view.getAllowedRoles());
		params.put("users", null);
		
		try 
		{
			return template.update(sql, params) == 1 ;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
		
	}

}
