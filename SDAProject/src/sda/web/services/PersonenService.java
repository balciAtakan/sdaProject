package sda.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import sda.web.daos.PersonDAO;
import sda.web.exception.SDAException;
import sda.web.util.SDAUtil;
import sda.web.util.UserRole;
import sda.web.views.PersonView;

@Service
@Scope("session")
public class PersonenService {
	
	@Autowired
	private PersonDAO personDAO;
	//active User,filled on login and lives through the session.
	private PersonView currUser;
	
	public PersonView getCurrentPersonDaten(String uuid) throws SDAException {
		
		return personDAO.findById(uuid);
	}
	
	public void updatePersonView(PersonView personView) {
		System.out.println("Person gespeichert: " + personView.toString());
	}

	public boolean checkCredentials(String username, String pass)
	{
		try 
		{
			currUser = new PersonView(personDAO.checkCredentials(username, pass));
		
			if(currUser == null)
				return false;
			else
				return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void createUser(PersonView person) throws SDAException
	{
		
		if(personDAO.userInDB(person.getUsername()))
			throw new SDAException("Username exist in Databank. Please enter another username!");
		
		person.setUuid(SDAUtil.GenerateUuid());
		personDAO.createUser(person);
		for(UserRole role :person.getRoles())
			personDAO.createUserRole(person, role);
	}
	
	public PersonView getCurrUser() {
		return currUser;
	}

	public void setCurrUser(PersonView currUser) {
		this.currUser = currUser;
	}
	
	
}
