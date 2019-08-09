package main.java.sda.web.backingbeans;

import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.SearchService;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.FacesContext;
import java.util.List;

@Component
@Scope("request")
public class QuickSearchBean {

	private static Logger log = LogManager.getLogger(QuickSearchBean.class);
	
	@Autowired
	private SearchService searchService;

	@Autowired
	private KnowledgeService knowledgeService;

	private String query;

	public void updateKnowledgeSearch(){

		searchService.selectQuickResultsByInput(query);
	}

	public String processOpenKnowledge(){

		reset();

		FacesContext context = FacesContext.getCurrentInstance();
		KnowledgeView view = context.getApplication().evaluateExpressionGet(context,"#{erg}", KnowledgeView.class);

		knowledgeService.setCurrentKnowledge(view);

		return "knowledge?faces-redirect=true";
	}

	public void setBackButtonActive(boolean value){
		knowledgeService.setBackButtonActive(value);
	}

	private void reset(){
		setQuery(null);
		searchService.setResults(null);
	}

	public List<KnowledgeView> getResults(){
		return searchService.getResults();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
