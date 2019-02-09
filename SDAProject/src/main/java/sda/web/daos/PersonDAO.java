package main.java.sda.web.daos;

import main.java.sda.web.daos.mapper.PersonRowMapper;
import main.java.sda.web.util.UserRole;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.SDAUtil;
import main.java.sda.web.views.PersonView;

import java.util.*;

@Repository
public class PersonDAO {

	private static Logger log = LogManager.getLogger(PersonDAO.class);

	@Autowired
	private NamedParameterJdbcTemplate template;

	public PersonView findById(String uuid) throws SDAException {
		// statt ? wird :uuid verwendet...
		String sql = "SELECT p.*, r.role_code FROM person p left join person_role r on p.id = r.person_id WHERE p.id=:id";

		Map<String, Object> parameters = 
				new HashMap<>();
		parameters.put("id", uuid);
		List<PersonView> result = new ArrayList<>();

		try {
		
			result = template.query(sql, parameters, new PersonRowMapper(true));
			
			if(result.size() > 0)
				for(int i = 1 ; i < result.size() ; i++)
					result.get(0).getRoles().addAll((result.get(i).getRoles()));
				
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error in DB!");
			throw new SDAException("Error in DB!");
		}
		
		return result.get(0);
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
			log.info("Error in DB!");
			throw new SDAException("Error in DB!");
		}
	}

	public PersonView checkCredentials(String uname, String pass)throws SDAException
	{
		String sql="select * from person where username=:uname and pass=:pass" ;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uname", uname);
		parameters.put("pass", pass);
		PersonView res = null;

		try {
			res = template.queryForObject(sql, parameters, new PersonRowMapper(false));
		}
		catch (EmptyResultDataAccessException e) {

			return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("noldu lan Error in DB!");
			throw new SDAException("Error in DB!");
		}

		return res;
	}
	
	public boolean createUser(PersonView person)throws SDAException{
		
		String sql = "insert into person values(:uuid,:firstname,:lastname,:username,:pass,:modify_date)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("uuid", person.getUuid());
		parameters.put("firstname", person.getFirstname().toLowerCase());
		parameters.put("lastname", person.getLastname().toLowerCase());
		parameters.put("username", person.getUsername().toLowerCase());
		parameters.put("modify_date", new Date());
		parameters.put("pass", person.getPassword());
		
		try {
			template.update(sql, parameters);
			log.info("User is inserted!");
		} 
		catch (DuplicateKeyException dke) {
			log.info("Username in DB!");
			throw new SDAException("Username exist in DB!");
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Error in DB!");
			throw new SDAException("Error in DB in createUser process!");
		}
		return true;
	}
	
	public boolean createUserRole(PersonView person, UserRole role)throws SDAException{
		
		String sql = "insert into person_role values(:id,:role_code,:role_description,:person_id)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", SDAUtil.generateUuid());
		parameters.put("role_code", role.name().toLowerCase());
		parameters.put("role_description", role.role().toLowerCase());
		parameters.put("person_id", person.getUuid());
		try {
			template.update(sql, parameters);
			log.info("Role is inserted!");
		} 
		catch (Exception e) {
			e.printStackTrace();
			log.info("Error in DB!");
			throw new SDAException("Error in DB!");
		}
		return true;
	}
	
	public boolean deleteUser(String username)throws SDAException{
		
		String sql = "DELETE FROM person WHERE username = :username";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("username", username);
		
		try {
			template.update(sql, parameters);
			log.info("User is deleted!");
		}
		catch (Exception e) {
			if(e instanceof DataIntegrityViolationException)
			{
				log.info("Roomowner!!");
				throw new SDAException("This user is a chatroom owner therefore cannot be deleted! Delete the Room First!");
			}
			else
			{
				e.printStackTrace();
				log.info("Error in DB!");
				throw new SDAException("Error in DB in deleteUser process!");
			}
		}
		return true;
	}

}
