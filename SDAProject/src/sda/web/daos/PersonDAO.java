package sda.web.daos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import sda.web.daos.mapper.PersonRowMapper;
import sda.web.exception.SDAException;
import sda.web.util.SDAUtil;
import sda.web.views.PersonView;

@Repository
public class PersonDAO {

	@Autowired
	private NamedParameterJdbcTemplate template;


	public PersonView findById(String uuid) {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT * FROM person WHERE id=:id";

		Map<String, Object> parameters = 
				new HashMap<>();
		parameters.put("id", uuid);

		return template.queryForObject(sql, parameters, 
				new PersonRowMapper());
	}

	public PersonView checkCredentials(String uname, String pass)throws SDAException{

		String sql="select * from person where username=:uname and pass=:pass" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uname", uname);
		parameters.put("pass", pass);
		PersonView res = null;
		try {
			res = template.queryForObject(sql, parameters, new PersonRowMapper());
		} 
		catch (EmptyResultDataAccessException e) {
			
			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in DB!");
			throw new SDAException("Error in DB!");
		}
		
		return res;
	}
	
	public boolean userInDB(String username)throws SDAException{
		
		String sql="select count(*) from person where username=:uname" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uname", username.toLowerCase());
		
		try {
			return template.queryForObject(sql, parameters, Integer.class) == 1;
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in DB!");
			throw new SDAException("Error in DB!");
		}
	}
	
	public boolean createUser(PersonView person)throws SDAException{
		
		String sql = "insert into person values(:uuid,:firstname,:lastname,:username,:pass,:role)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uuid", SDAUtil.GenerateUuid());
		parameters.put("firstname", person.getFirstname().toLowerCase());
		parameters.put("lastname", person.getLastname().toLowerCase());
		parameters.put("username", person.getUsername().toLowerCase());
		parameters.put("pass", person.getPassword());
		parameters.put("role", person.getRole());
		
		try {
			template.update(sql, parameters);
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in DB!");
			throw new SDAException("Error in DB!");
		}
		return true;
	}

}
