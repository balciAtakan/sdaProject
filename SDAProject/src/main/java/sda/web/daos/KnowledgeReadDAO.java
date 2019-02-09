package main.java.sda.web.daos;

import main.java.sda.web.daos.mapper.KnowledgeRowMapper;
import main.java.sda.web.util.SDAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.views.KnowledgeView;

import java.util.*;

@Repository
public class KnowledgeReadDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;

	public ArrayList<KnowledgeView> getAllKnowledge() throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT k.*,p.username FROM knowledge k left join person p on k.room_owner = p.id";
		Map<String, Object> params = new HashMap<>();

		ArrayList<KnowledgeView> res;
		try {

			res = new ArrayList<>(template.query(sql, params, new KnowledgeRowMapper()));
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
		
		return res;
	}

	public boolean saveKnowledge(KnowledgeView view) throws SDAException {

		//language=MySQL
		String sql = "insert into knowledge values(:uuid,:word,:category,:knowledge_text,:knowledge_data,:modify_date,:owner,:subcategory)";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("uuid", SDAUtil.generateUuid());
		parameters.put("word", view.getWord());
		parameters.put("category", view.getDfXCategory().name());
		parameters.put("knowledge_text", view.getKnowledge_text());
		parameters.put("knowledge_data", view.getFilename());
		parameters.put("modify_date", new Date());
		parameters.put("owner", view.getOwner());
		parameters.put("subcategory", view.getDfXSubCategory() != null ? view.getDfXSubCategory().name() : null);

		boolean res;
		try {

			res = template.update(sql, parameters) == 1;

		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}

		return res;
	}
	
}
