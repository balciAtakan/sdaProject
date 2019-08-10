package main.java.sda.web.daos;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.SDAUtil;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import main.java.sda.web.views.PersonView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class KnowledgeRoomWriteDAO
{

    private static Logger log = LogManager.getLogger(KnowledgeRoomWriteDAO.class);

    @Autowired
    private NamedParameterJdbcTemplate template;

    public KnowledgeRoomView saveKnowledgeRoom(KnowledgeRoomView view) throws SDAException
    {
        // statt ? wird :uuid verwendet...

        view.setUuid(SDAUtil.generateUuid());

        String sql = "INSERT INTO knowledge_room(uuid,roomname,room_owner,date_create) VALUES(:uuid , :roomname , :roomOwner, :date_create)";

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", view.getUuid());
        params.put("roomname", view.getRoomname());
        params.put("roomOwner", view.getRoomOwner());
        params.put("date_create", new Date());

        try
        {
            template.update(sql, params);

            return view;
        } catch (Exception e)
        {
            e.printStackTrace();
            throw new SDAException(e.getMessage());
        }
    }

    public boolean saveKnowledgeRoomMessage(KnowledgeRoomMessageView view) throws SDAException
    {
        String sql = "INSERT INTO knowledge_room_message(uuid,content,owner,modify_date,knowledge_room) VALUES(:uuid , :content , :owner, :modify_date,:kr)";

        Map<String, Object> params = new HashMap<>();
        params.put("uuid", view.getUuid());
        params.put("content", view.getMessage());
        params.put("owner", view.getMessageOwner().getUsername());
        params.put("modify_date", view.getMessageDate());
        params.put("kr", view.getKnowledgeRoomId());

        return doInsert(sql, params);
    }

    private boolean doInsert(String sql, Map<String, Object> params) throws SDAException
    {
        try
        {
            return template.update(sql, params) == 1;
        } catch (Exception e)
        {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new SDAException(e.getMessage());
        }
    }

    public boolean insertKnowledgeRoomRoles(KnowledgeRoomView view) throws SDAException
    {

        StringBuilder sql = new StringBuilder(
                "INSERT INTO knowledge_room_role(id,role_code,knowledge_room_id) VALUES");

        for (int i = 0; i < view.getAllowedRoles().size(); i++)
        {

            sql.append(" (:id").append(i).append(",:role_code").append(i).append(
                    ", :knowledge_room_id").append(i).append(")");
            sql.append((i == view.getAllowedRoles().size() - 1) ? ";" : ",");
        }

        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < view.getAllowedRoles().size(); i++)
        {

            params.put("id" + i, SDAUtil.generateUuid());
            params.put("role_code" + i, view.getAllowedRoles().get(i).name().toLowerCase());
            params.put("knowledge_room_id" + i, view.getUuid());
        }

        try
        {
            return template.update(sql.toString(), params) != 0;
        } catch (Exception e)
        {
            e.printStackTrace();
            log.info(e.getMessage());
            throw new SDAException(e.getMessage());
        }
    }

    public boolean saveKnowledgeRoomUser(KnowledgeRoomView room, PersonView person) throws SDAException
    {
        String sql = "INSERT INTO knowledge_room_user(id,username,knowledge_room_id,person_id) VALUES(:id,:username,:knowledge_room_id,:person_id)";

        Map<String, Object> params = new HashMap<>();
        params.put("id", SDAUtil.generateUuid());
        params.put("username", person.getUsername());
        params.put("knowledge_room_id", room.getUuid());
        params.put("person_id", person.getUuid());

        return doInsert(sql, params);
    }
}
