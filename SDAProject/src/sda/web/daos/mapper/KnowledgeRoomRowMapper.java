package sda.web.daos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.jdbc.core.RowMapper;

import sda.web.util.UserRole;
import sda.web.views.KnowledgeRoomView;
import sda.web.views.PersonView;

public class KnowledgeRoomRowMapper implements RowMapper<KnowledgeRoomView> {

	@Override
	public KnowledgeRoomView mapRow(ResultSet resultSet, int arg1) throws SQLException {
		KnowledgeRoomView roomView = new KnowledgeRoomView();
		roomView.setUuid(resultSet.getString("uuid"));
		roomView.setRoomname(resultSet.getString("roomname"));
		roomView.setRoomOwner(resultSet.getString("room_owner"));
		roomView.setDateCreate(resultSet.getDate("date_create"));
		
		ArrayList<PersonView> list = new  ArrayList<PersonView>();
		list.add(new PersonView(resultSet.getString("person_id"),resultSet.getString("username")));
		
		roomView.setUsers(list);
		
		ArrayList<UserRole> list2 = new ArrayList<>();
		list2.add(UserRole.getEnum(resultSet.getString("role_code").toLowerCase()));
		
		roomView.setAllowedRoles(list2);
		
		return roomView;
		
	}


}
