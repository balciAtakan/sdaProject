package main.java.sda.web.daos.mapper;

import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.PersonView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class KnowledgeRoomMessageRowMapper implements RowMapper<KnowledgeRoomMessageView>
{

    @Override
    public KnowledgeRoomMessageView mapRow(ResultSet resultSet, int arg1) throws SQLException
    {
        KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView(resultSet.getString("uuid"),
                resultSet.getString("content"));

        messageView.setKnowledgeRoomId(resultSet.getString("knowledge_room"));

        messageView.setMessageOwner(new PersonView(resultSet.getString("owner"), resultSet.getString("username")));

        Timestamp timestamp = resultSet.getTimestamp("date");
        if (timestamp != null)
            messageView.setMessageDate(new java.util.Date(timestamp.getTime()));

        return messageView;

    }


}
