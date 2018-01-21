package sda.web.daos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sda.web.util.SDAUtil;
import sda.web.views.KnowledgeRoomView;

public class KnowledgeRoomRowMapper implements RowMapper<KnowledgeRoomView> {

	@Override
	public KnowledgeRoomView mapRow(ResultSet resultSet, int arg1) throws SQLException {
		KnowledgeRoomView roomView = new KnowledgeRoomView();
		roomView.setUuid(resultSet.getString("uuid"));
		roomView.setRoomname(resultSet.getString("roomname"));
	
		roomView.setAllowedRoles(SDAUtil.seperateUserRoles(resultSet.getString("user_roles").toUpperCase()));
	
		
		//personView.setUsername(resultSet.getString("users"));
		return roomView;
		
	}


}
