package main.java.sda.web.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import main.java.sda.web.daos.KnowledgeDAO;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.views.KnowledgeView;

import java.util.List;

@Service
@Scope("session")
public class KnowledgeService {

	private static Logger log = LogManager.getLogger(KnowledgeService.class);

	@Autowired
	private KnowledgeDAO knowledgeReadDAO;

	private List<KnowledgeView> allKnowledge;
	private KnowledgeView currentKnowledge;
	
	public void initAllKnowledge() throws SDAException{
		
		allKnowledge = knowledgeReadDAO.getAllKnowledge();
		log.info("all knowledge has been loaded! Count : " + (allKnowledge != null ? allKnowledge.size(): " "));
	}

	public boolean saveKnowledge(KnowledgeView view) throws SDAException{

		return knowledgeReadDAO.saveKnowledge(view);
	}

	public void initKnowledge(){

		try {
			currentKnowledge = knowledgeReadDAO.getKnowledge(currentKnowledge);
		} catch (SDAException e) {
			e.printStackTrace();
			currentKnowledge = null;
		}
	}

	public List<KnowledgeView> getAllKnowledge() {
		return allKnowledge;
	}

	public void setAllKnowledge(List<KnowledgeView> allKnowledge) {
		this.allKnowledge = allKnowledge;
	}

	public KnowledgeView getCurrentKnowledge() {
		return currentKnowledge;
	}

	public void setCurrentKnowledge(KnowledgeView currentKnowledge) {
		this.currentKnowledge = currentKnowledge;
	}
}
