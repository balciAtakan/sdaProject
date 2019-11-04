package main.java.sda.web.backingbeans;

import main.java.sda.web.services.KnowledgeRoomService;
import main.java.sda.web.services.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
public class StrukturBean
{

    @Autowired
    private KnowledgeRoomService knowledgeRoomService;

    @Autowired
    private KnowledgeService knowledgeService;

    public String logout()
    {
        //personenService.setCurrUser(null);
        return "login?faces-redirect=true";
    }

    public String processHome()
    {
        knowledgeService.reset();
        knowledgeRoomService.reset();
        return "home?faces-redirect=true";
    }

    public String processCommunication()
    {
        return "communication?faces-redirect=true";
    }

    public String processCreateKnowledge()
    {

        knowledgeRoomService.reset();
        return "createKnowledge?faces-redirect=true";
    }

    public String processSearchKnowledge()
    {
        return "searchKnowledge?faces-redirect=true";
    }

}
