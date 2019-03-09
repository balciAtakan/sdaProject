package main.java.sda.web.daos;

import main.java.sda.web.daos.mapper.KnowledgeRowMapper;
import main.java.sda.web.daos.mapper.KnowledgeRowQSMapper;
import main.java.sda.web.util.SDAUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.views.KnowledgeView;

import java.io.FileInputStream;
import java.util.*;

@Repository
public class KnowledgeDAO {


    private static Logger log = LogManager.getLogger(KnowledgeDAO.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	public KnowledgeView getKnowledge(KnowledgeView view) throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT k.*,p.username FROM knowledge k left join person p on k.owner = p.id where k.uuid = :uuid";
		Map<String, Object> params = new HashMap<>();

		params.put("uuid",view.getUuid());

		try {

			return template.queryForObject(sql, params, new KnowledgeRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
	}

	public List<KnowledgeView> getKnowledgeFromWord(String word) throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT k.*,p.username FROM knowledge k left join person p on k.owner = p.id where k.word = :word";
		Map<String, Object> params = new HashMap<>();

		params.put("word", word);

		try {

			return template.query(sql, params, new KnowledgeRowMapper());

		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}
	}

	public ArrayList<KnowledgeView> getAllKnowledge() throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT k.*,p.username FROM knowledge k left join person p on k.owner = p.id";
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



		boolean res;
		try {
			//language=MySQL
			String sql = "insert into knowledge values(:uuid,:word,:category,:knowledge_text,:knowledge_data,:modify_date,:owner,:subcategory)";
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("uuid", SDAUtil.generateUuid());
			parameters.put("word", view.getWord());
			parameters.put("category", view.getDfXCategory().name());
			parameters.put("knowledge_text", view.getKnowledge_text());
			parameters.put("knowledge_data", view.getFileUpload() == null ? null : view.getFileUpload());
			parameters.put("modify_date", new Date());
			parameters.put("owner", view.getOwnerID());
			parameters.put("subcategory", view.getDfXSubCategory() != null ? view.getDfXSubCategory().name() : null);

			res = template.update(sql, parameters) == 1;
			log.info("update result: " + res);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SDAException(e.getMessage());
		}

		return res;
	}

    public ArrayList<KnowledgeView> selectQuickResultsByInput(String input)
    {
        if(input != null && input.length() > 0)
        {
            //template = new NamedParameterJdbcTemplate(schuljahrDatasource);
//			input = input.replace(" ", "%");
            String[] allinputs = input.split("\\s");


            String sql = generateQuickSearchSQL();

            Map<String, Object> params = new HashMap<>();

            int counter = 0;

            for(String oneinput : allinputs)
            {
                if(oneinput != null && !oneinput.equals(" ") && !oneinput.equals(""))
                {
                    if(counter != 0)
                        sql = sql + " or ";
                    sql = sql + " lower(word) like :input" + counter;

                    //Params für namedQuery setzen!
                    params.put("input" + counter, "%" + oneinput.toLowerCase().trim() + "%");

                    counter++;
                }
            }

            sql = sql + " order by word limit 10";

            log.info(sql);


            //Absetzen der Query und Ergebnis erhalten
            return (ArrayList<KnowledgeView>)template.query(sql, params, new KnowledgeRowQSMapper());
        }
        else
            return new ArrayList<>();
    }

    private String generateQuickSearchSQL(){

	    StringBuilder sb = new StringBuilder();

	    sb.append(" SELECT * from knowledge where ");

	    return sb.toString();
    }


}
