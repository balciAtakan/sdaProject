package main.java.sda.web.backingbeans;

import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.QuickSearchService;
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
	private QuickSearchService quickSearchService;

	@Autowired
	private KnowledgeService knowledgeService;

	private String query;

	public void updateKnowledgeSearch(){

		quickSearchService.selectQuickResultsByInput(query);
	}

	public String processOpenKnowledge(){

		reset();

		FacesContext context = FacesContext.getCurrentInstance();
		KnowledgeView view = context.getApplication().evaluateExpressionGet(context,"#{erg}", KnowledgeView.class);

		knowledgeService.setCurrentKnowledge(view);

		return "knowledge?faces-redirect=true";
	}

	private void reset(){
		setQuery(null);
		quickSearchService.setResults(null);
	}

	public List<KnowledgeView> getResults(){
		return quickSearchService.getResults();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}