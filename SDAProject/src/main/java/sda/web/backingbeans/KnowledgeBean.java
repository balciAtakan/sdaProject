package main.java.sda.web.backingbeans;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.List;

@Component
@Scope("view")
public class KnowledgeBean {

	private static Logger log = LogManager.getLogger(KnowledgeBean.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private PersonenService personenService;

	private KnowledgeView view;
	private List<SelectItem> dfxCategories;
	private String selectedCategory;

	private boolean updateActive;
	
	@PostConstruct
	public void init(){
		log.info("Knowledge bean init!");

		knowledgeService.initKnowledge();
		setView(knowledgeService.getCurrentKnowledge());
		dfxCategories = knowledgeService.initCategories();
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

	public void processUpdateKnowledge() {

		try {

			if(selectedCategory != null && !selectedCategory.isEmpty()) {
				if (selectedCategory.contains("SubCategory")) {
					log.info("chosen no sub category: " + selectedCategory.substring(15));
					view.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(15)));
					view.setDfXSubCategory(null);
				} else {
					log.info("chosen category: " + selectedCategory);
					view.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(0, 4).trim()));
					view.setDfXSubCategory(DfXSubCategory.getEnum(selectedCategory, true));
				}
			}

			view.setOwnerID(personenService.getCurrUser().getUuid());
			knowledgeService.updateKnowledge(view);
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Knowledge has been updated",""));
		} catch (SDAException e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
		}
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

	public List<SelectItem> getDfxCategories() {
		return dfxCategories;
	}

	public void setDfxCategories(List<SelectItem> dfxCategories) {
		this.dfxCategories = dfxCategories;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
}
