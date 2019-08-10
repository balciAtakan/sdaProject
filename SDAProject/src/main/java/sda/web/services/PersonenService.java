package main.java.sda.web.services;

import main.java.sda.web.daos.PersonDAO;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.SDAUtil;
import main.java.sda.web.util.UserRole;
import main.java.sda.web.views.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("session")
public class PersonenService
{

    @Autowired
    private PersonDAO personDAO;
    //active User,filled on login and lives through the session.
    private PersonView currUser;

    public PersonView getCurrentPersonDaten(String uuid) throws SDAException
    {

        if (currUser == null) currUser = personDAO.findById(uuid);

        return currUser;

    }

    public void updatePersonView(PersonView personView)
    {
    }

    public boolean checkCredentials(String username, String pass)
    {
        try
        {
            currUser = new PersonView(personDAO.checkCredentials(username, pass));

            if (currUser == null) return false;
            else return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public void createUser(PersonView person) throws SDAException
    {
        person.setUuid(SDAUtil.generateUuid());
        personDAO.createUser(person);
        for (UserRole role : person.getRoles())
            personDAO.createUserRole(person, role);
    }

    public void deleteUser(String username) throws SDAException
    {
        personDAO.deleteUser(username);
    }

    public PersonView getCurrUser()
    {
        return currUser;
    }

    public void setCurrUser(PersonView currUser)
    {
        this.currUser = currUser;
    }


}
