package main.java.sda.web.daos.mapper;

import main.java.sda.web.util.UserRole;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import main.java.sda.web.views.PersonView;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class KnowledgeRoomRowMapper implements RowMapper<KnowledgeRoomView>
{

    @Override
    public KnowledgeRoomView mapRow(ResultSet resultSet, int arg1) throws SQLException
    {
        KnowledgeRoomView roomView = new KnowledgeRoomView();
        roomView.setUuid(resultSet.getString("uuid"));
        roomView.setRoomname(resultSet.getString("roomname"));
        roomView.setRoomOwner(resultSet.getString("room_owner"));
        roomView.setDateCreate(resultSet.getDate("date_create"));

        ArrayList<PersonView> list = new ArrayList<PersonView>();
        list.add(new PersonView(resultSet.getString("person_id"), resultSet.getString("username")));

        roomView.setUsers(list);

        ArrayList<UserRole> list2 = new ArrayList<>();
        list2.add(UserRole.getEnum(resultSet.getString("role_code").toLowerCase()));

        roomView.setAllowedRoles(list2);

        ArrayList<KnowledgeRoomMessageView> list3 = new ArrayList<>();

        if (resultSet.getString("krm_uuid") != null)
        {

            PersonView owner = new PersonView();
            owner.setUsername(resultSet.getString("owner"));
            Timestamp timestamp = resultSet.getTimestamp("modify_date");
            KnowledgeRoomMessageView history = new KnowledgeRoomMessageView(resultSet.getString("krm_uuid"),
                    resultSet.getString("content"), timestamp != null ? new java.util.Date(timestamp.getTime()) : null,
                    owner, resultSet.getString("uuid"));
            list3.add(history);
        }

        roomView.setHistory(list3);

        return roomView;

    }


}
