package main.java.sda.web.services;

import main.java.sda.web.daos.KnowledgeReadDAO;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class QuickSearchService {

	private static Logger log = LogManager.getLogger(QuickSearchService.class);

	@Autowired
	private KnowledgeReadDAO knowledgeReadDAO;

	private List<KnowledgeView> results;
	
	public void selectQuickResultsByInput(String input){

		results = knowledgeReadDAO.selectQuickResultsByInput(input);
		log.info("all knowledge has been loaded! Count : " + (results != null ? results.size(): " "));
	}

	public List<KnowledgeView> getResults() {
		return results;
	}

	public void setResults(List<KnowledgeView> results) {
		this.results = results;
	}
}
