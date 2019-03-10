package main.java.sda.web.backingbeans;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Scope("view")
public class CreateKnowledgeBean {

	private static Logger log = LogManager.getLogger(CreateKnowledgeBean.class);

	@Autowired
	private KnowledgeService knowledgeService;

	@Autowired
	private PersonenService personenService;

	private KnowledgeView view;
	private UploadedFile fileUpload;
	private InputStream stream;
	private List<SelectItem> dfxCategories;
	private String selectedCategory;
	
	@PostConstruct
	public void init(){
		log.info("Create Knowledge bean init!");
		view = new KnowledgeView();
		dfxCategories = knowledgeService.initCategories();

	}
	public void handleFileUpload(FileUploadEvent event) {
		log.info("file upload ");
		FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		fileUpload = event.getFile();
		try {
			stream = fileUpload.getInputstream();
			log.info("working! ");
		} catch (IOException e) {
			e.printStackTrace();
			log.info("not working! ");
		}
	}

	public String addKnowledge(){
		if(selectedCategory == null) {
			return null;
		}
		if(selectedCategory.contains("SubCategory")) {
			log.info("chosen no sub category: " + selectedCategory.substring(15));
			view.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(15)));
		}
		else
		{
			log.info("chosen category: " +selectedCategory);
			view.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(0,4).trim()));
			view.setDfXSubCategory(DfXSubCategory.getEnum(selectedCategory, true));
		}

		view.setOwnerID(personenService.getCurrUser().getUuid());

		if(stream != null)
			view.setFileUpload(stream);

		try {
			if(knowledgeService.saveKnowledge(view))
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Knowledge successfuly saved!",""));

		}catch (SDAException e)
		{

			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			e.printStackTrace();
			log.info(e.getMessage());
		}

		return "home?faces-redirect=true";
	}

	public KnowledgeView getView() {
		return view;
	}

	public void setView(KnowledgeView view) {
		this.view = view;
	}

	public String getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

	public List<SelectItem> getDfxCategories() {
		return dfxCategories;
	}

	public void setDfxCategories(List<SelectItem> dfxCategories) {
		this.dfxCategories = dfxCategories;
	}
}
