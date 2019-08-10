package main.java.sda.web.services;

import main.java.sda.web.daos.KnowledgeDAO;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class SearchService
{

    private static Logger log = LogManager.getLogger(SearchService.class);

    @Autowired
    private KnowledgeDAO knowledgeDAO;

    private List<KnowledgeView> results;

    public void selectQuickResultsByInput(String input)
    {

        results = knowledgeDAO.selectQuickResultsByInput(input);
        log.info("all knowledge has been loaded! Count : " + (results != null ? results.size() : " "));
    }

    public List<KnowledgeView> selectSearchResultsByInput(KnowledgeView input)
    {

        List<KnowledgeView> res = knowledgeDAO.selectSearchResultsByInput(input);
        log.info("Search results has been loaded! Count : " + (res != null ? res.size() : " "));
        return res;
    }

    public List<KnowledgeView> getResults()
    {
        return results;
    }

    public void setResults(List<KnowledgeView> results)
    {
        this.results = results;
    }
}
