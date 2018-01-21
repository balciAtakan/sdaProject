package sda.web.daos.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import sda.web.views.PersonView;

public class PersonRowMapper implements RowMapper<PersonView> {

	@Override
	public PersonView mapRow(ResultSet resultSet, int arg1) throws SQLException {
		PersonView personView = new PersonView();
		personView.setUuid(resultSet.getString("id"));
		personView.setFirstname(resultSet.getString("firstname"));
		personView.setLastname(resultSet.getString("lastname"));
		personView.setUsername(resultSet.getString("username"));
		personView.setRole(resultSet.getString("role"));
		return personView;
		
	}


}
