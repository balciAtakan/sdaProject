package main.java.sda.web.daos;

import main.java.sda.web.daos.mapper.KnowledgeRowMapper;
import main.java.sda.web.daos.mapper.KnowledgeRowQSMapper;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.SDAUtil;
import main.java.sda.web.views.KnowledgeView;
import main.java.sda.web.views.WordView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class KnowledgeDAO
{


    private static Logger log = LogManager.getLogger(KnowledgeDAO.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    public List<KnowledgeView> getKnowledge(KnowledgeView view) throws SDAException
    {
        // statt ? wird :uuid verwendet...
        String sql = "SELECT k.*,p.username, syn.word as syn_word, syn.score FROM knowledge k " +
                "left join person p on k.owner = p.id " +
                "left join knowledge_synonym syn on syn.knowledge_id = k.uuid where k.uuid = :uuid";
        Map<String, Object> params = new HashMap<>();

        params.put("uuid", view.getUuid());

        try
        {

            return template.query(sql, params, new KnowledgeRowMapper());

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }
    }

    public List<KnowledgeView> getKnowledgeFromWord(String word) throws SDAException
    {
        // statt ? wird :uuid verwendet...
        String sql = "SELECT k.*,p.username,sy.score, sy.word as syn_word, sy.knowledge_id FROM knowledge k " +
                "left join person p on k.owner = p.id " +
                "left join knowledge_synonym sy on k.uuid = sy.knowledge_id where k.word = :word ";
        Map<String, Object> params = new HashMap<>();

        params.put("word", word);

        try
        {

            return template.query(sql, params, new KnowledgeRowMapper());

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }
    }

    public ArrayList<KnowledgeView> getAllKnowledge() throws SDAException
    {
        // statt ? wird :uuid verwendet...
        String sql = "SELECT k.*,p.username, syn.word as syn_word, syn.score FROM knowledge k " +
                "left join person p on k.owner = p.id " +
                "left join knowledge_synonym syn on syn.knowledge_id = k.uuid";
        Map<String, Object> params = new HashMap<>();

        ArrayList<KnowledgeView> res;
        try
        {

            res = new ArrayList<>(template.query(sql, params, new KnowledgeRowMapper()));

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }

        return res;
    }

    public KnowledgeView saveKnowledge(KnowledgeView view) throws SDAException
    {


        boolean res;
        try
        {
            //language=MySQL
            String sql = "insert into knowledge values(:uuid,:word,:category,:knowledge_text,:knowledge_data,:modify_date,:owner,:subcategory)";
            Map<String, Object> parameters = new HashMap<>();
            view.setUuid(SDAUtil.generateUuid());
            parameters.put("uuid", view.getUuid());
            parameters.put("word", view.getWord());
            parameters.put("category", view.getDfXCategory().name());
            parameters.put("knowledge_text", view.getKnowledge_text());
            parameters.put("knowledge_data", view.getFileUpload());
            parameters.put("modify_date", new Date());
            parameters.put("owner", view.getOwnerID());
            parameters.put("subcategory", view.getDfXSubCategory() != null ? view.getDfXSubCategory().name() : null);

            res = template.update(sql, parameters) == 1;
            log.info("update result: " + res);

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }

        return view;
    }

    public boolean saveSynonyms(List<WordView> views, String knowledge_id) throws SDAException
    {

        boolean res = true;
        try
        {
            for ( WordView view: views) {
                //language=MySQL
                String sql = "insert into knowledge_synonym values(:uuid,:word,:score,:knowledge_id)";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("uuid", SDAUtil.generateUuid());
                parameters.put("word", view.getWord());
                parameters.put("score", view.getScore());
                parameters.put("knowledge_id", knowledge_id);

                res = template.update(sql, parameters) == 1;
                log.info("synonym save result: " + res);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }

        return res;
    }

    public ArrayList<KnowledgeView> selectQuickResultsByInput(String input)
    {
        if (input != null && input.length() > 0)
        {
            //template = new NamedParameterJdbcTemplate(schuljahrDatasource);
//			input = input.replace(" ", "%");
            String[] allinputs = input.split("\\s");


            String sql = generateQuickSearchSQL();

            Map<String, Object> params = new HashMap<>();

            int counter = 0;

            for (String oneinput : allinputs)
            {
                if (oneinput != null && !oneinput.equals(" ") && !oneinput.equals(""))
                {
                    if (counter != 0) sql = sql + " or ";
                    sql = sql + " lower(word) like :input" + counter;

                    //Params für namedQuery setzen!
                    params.put("input" + counter, "%" + oneinput.toLowerCase().trim() + "%");

                    counter++;
                }
            }

            sql = sql + " order by word limit 10";

            log.info(sql);


            //Absetzen der Query und Ergebnis erhalten
            return (ArrayList<KnowledgeView>) template.query(sql, params, new KnowledgeRowQSMapper());
        } else return new ArrayList<>();
    }

    private String generateQuickSearchSQL()
    {

        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * from knowledge where ");

        return sb.toString();
    }

    public ArrayList<KnowledgeView> selectSearchResultsByInput(KnowledgeView view)
    {

        Map<String, Object> params = new HashMap<>();
        String sql = generateQuickSearchSQL();

        if (view.getDfXCategory() != null)
        {
            sql += "category = :category " + (view.getDfXSubCategory() != null || (view.getWord() != null && !view.getWord().isEmpty()) ? "and " : "");
            params.put("category", view.getDfXCategory().name());
        }

        if (view.getDfXSubCategory() != null)
        {
            sql += "subcategory = :subcategory " + (view.getWord() != null && !view.getWord().isEmpty() ? "and " : "");
            params.put("subcategory", view.getDfXSubCategory().name());
        }

        if (view.getWord() != null && view.getWord().length() > 0)
        {
//			input = input.replace(" ", "%");
            String[] allinputs = view.getWord().split("\\s");

            int counter = 0;
            int end = allinputs.length;

            for (String oneinput : allinputs)
            {
                if (oneinput != null && !oneinput.equals(" ") && !oneinput.equals(""))
                {
                    sql += counter != 0 ? " or " : "(";
                    sql = sql + " lower(word) like :input" + counter;
                    sql += counter == end - 1 ? ")" : "";

                    //Params für namedQuery setzen!
                    params.put("input" + counter, "%" + oneinput.toLowerCase().trim() + "%");

                    counter++;
                }
            }

        }

        sql = sql + " order by word";

        log.info(sql);
        try
        {
            return (ArrayList<KnowledgeView>) template.query(sql, params, new KnowledgeRowQSMapper());
        } catch (EmptyResultDataAccessException e)
        {

            return new ArrayList<>();
        }
    }

    public void updateKnowledge(KnowledgeView view) throws SDAException
    {
        String sql = "update knowledge set knowledge_text = :ktext, category = :category,subcategory =:subcategory, modify_date =:mdate, owner =:ownerId " + " where uuid = :uuid";

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", view.getUuid());
        params.put("ktext", view.getKnowledge_text());
        params.put("category", view.getDfXCategory().name());
        params.put("subcategory", view.getDfXSubCategory() != null ? view.getDfXSubCategory().name() : null);
        params.put("mdate", new Date());
        params.put("ownerId", view.getOwnerID());

        try
        {

            int res = template.update(sql, params);
            log.info("Knowledge has been updated! " + (res == 1));

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException("Error on Knowledge update!");
        }
    }

    public void deleteKnowledge(String uuid) throws SDAException
    {
        String sql = "delete from knowledge where uuid = :uuid";
        Map<String, Object> params = new HashMap<>();
        params.put("uuid", uuid);

        try
        {

            int res = template.update(sql, params);
            log.info("Knowledge has been deleted! " + (res == 1));

        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }
    }

    public int getKnowledgeCount() throws SDAException
    {
        String sql = "SELECT count(*) FROM knowledge";

        Map<String, Object> params = new HashMap<>();

        try
        {
            Integer res = template.queryForObject(sql, params, Integer.class);
            return res == null ? 0 : res;

        } catch (Exception e)
        {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new SDAException(e.getMessage());
        }
    }

}
