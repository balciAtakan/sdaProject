package sda.web.daos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import sda.web.util.SDAUtil;
import sda.web.views.KnowledgeRoomView;
import sda.web.views.PersonView;

public class KnowledgeRoomRowMapper implements RowMapper<KnowledgeRoomView> {

	@Override
	public KnowledgeRoomView mapRow(ResultSet resultSet, int arg1) throws SQLException {
		KnowledgeRoomView roomView = new KnowledgeRoomView();
		roomView.setUuid(resultSet.getString("uuid"));
		roomView.setRoomname(resultSet.getString("roomname"));
		roomView.setRoomOwner(resultSet.getString("room_owner"));
		
		List<String> temp = SDAUtil.seperateUserIds(resultSet.getString("users"));
		ArrayList<PersonView> list =new  ArrayList<PersonView>();
		if(temp != null)
			temp.stream().forEach(id -> list.add(new PersonView(id)));
		roomView.setUsers(list);
		
		roomView.setAllowedRoles(SDAUtil.seperateUserRoles(resultSet.getString("user_roles").toUpperCase()));
	
		
		//personView.setUsername(resultSet.getString("users"));
		return roomView;
		
	}


}
