package sda.web.daos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sda.web.views.KnowledgeRoomMessageView;
import sda.web.views.PersonView;

public class KnowledgeRoomMessageRowMapper implements RowMapper<KnowledgeRoomMessageView> {

	@Override
	public KnowledgeRoomMessageView mapRow(ResultSet resultSet, int arg1) throws SQLException {
		KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView();
		messageView.setUuid(resultSet.getString("uuid"));
		messageView.setMessage(resultSet.getString("content"));
		messageView.setKnowledgeRoomId(resultSet.getString("knowledge_room"));

		messageView.setMessageOwner(new PersonView(resultSet.getString("owner"),resultSet.getString("username")));

		messageView.setMessageDate(resultSet.getDate("date"));
		
		
		return messageView;
		
	}


}
