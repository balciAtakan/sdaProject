package main.java.sda.web.backingbeans;

import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Scope("view")
public class KnowledgeBean {

	private static Logger log = LogManager.getLogger(KnowledgeBean.class);

	@Autowired
	private KnowledgeService knowledgeService;

	private KnowledgeView view;
	
	@PostConstruct
	public void init(){
		log.info("Knowledge bean init!");

		knowledgeService.initKnowledge();
		setView(knowledgeService.getCurrentKnowledge());
	}
	
	/*public String processDeleteUser() {

		try {
			if(usernameForDelete != null && !usernameForDelete.isEmpty()){
				personenService.deleteUser(usernameForDelete);
				log.info("User is deleted!");
				FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_INFO,"User is deleted",""));
			}
			if(roomnameForDelete != null && !roomnameForDelete.isEmpty()){
				knowledgeRoomService.deleteKnowledgeRoom(roomnameForDelete);
				log.info("Room is deleted!");
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Room is deleted",""));
			}
			
		} catch (SDAException e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			return null;
		}
		
		return "home?faces-redirect=true";
	}*/

	public KnowledgeView getView() {
		return view;
	}

	public void setView(KnowledgeView view) {
		this.view = view;
	}
}
