package sda.web.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.daos.mapper.KnowledgeRoomMessageRowMapper;
import sda.web.daos.mapper.KnowledgeRoomRowMapper;
import sda.web.exception.SDAException;
import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.KnowledgeRoomView;

@Repository
public class KnowledgeRoomReadDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public ArrayList<KnowledgeRoomView> getKnowledgeRooms() throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT * FROM knowledge_room kr, person p where kr.room_owner = p.id";

		Map<String, Object> params = new HashMap<>();
		ArrayList<KnowledgeRoomView> res = null;
		try {
			res = (ArrayList<KnowledgeRoomView>) template.query(sql, params,
					new KnowledgeRoomRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}

	public ArrayList<KnowledgeRoomMessageView> getKnowledgeRoomData(String roomId) throws SDAException {
		String sql = "SELECT * FROM knowledge_room_message krm, person p where knowledge_room = :roomId and krm.owner=p.id";

		Map<String, Object> params = new HashMap<>();

		params.put("roomId", roomId);
		ArrayList<KnowledgeRoomMessageView> res = null;
		try {
			res = (ArrayList<KnowledgeRoomMessageView>) template.query(sql, params,
					new KnowledgeRoomMessageRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}
}
