package main.java.sda.web.backingbeans;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Component
@Scope("view")
public class KnowledgeBean {

	private static Logger log = LogManager.getLogger(KnowledgeBean.class);

	@Autowired
	private KnowledgeService knowledgeService;

	private KnowledgeView view;

	private boolean updateActive;
	
	@PostConstruct
	public void init(){
		log.info("Knowledge bean init!");

		knowledgeService.initKnowledge();
		setView(knowledgeService.getCurrentKnowledge());
	}
	
	public String processDeleteKnowledge() {

		try {

			knowledgeService.deleteKnowledge(view.getUuid());
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Knowledge has been deleted",""));
		} catch (SDAException e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			return null;
		}
		
		return "home?faces-redirect=true";
	}

	public KnowledgeView getView() {
		return view;
	}

	public void setView(KnowledgeView view) {
		this.view = view;
	}

	public boolean isUpdateActive() {
		return updateActive;
	}

	public void setUpdateActive(boolean updateActive) {
		this.updateActive = updateActive;
	}
}
