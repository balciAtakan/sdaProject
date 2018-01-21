package sda.web.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.daos.mapper.KnowledgeRoomRowMapper;
import sda.web.exception.SDAException;
import sda.web.views.KnowledgeRoomView;

@Repository
public class KnowledgeRoomReadDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public ArrayList<KnowledgeRoomView> getKnowledgeRooms() throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT * FROM knowledge_room";

		Map<String, Object> parameters = new HashMap<>();
		ArrayList<KnowledgeRoomView> res = null;
		try {
			res = (ArrayList<KnowledgeRoomView>) template.query(sql, parameters,
					new KnowledgeRoomRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}

}
