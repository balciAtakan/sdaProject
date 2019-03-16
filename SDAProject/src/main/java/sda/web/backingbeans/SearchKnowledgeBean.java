package main.java.sda.web.backingbeans;

import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.services.SearchService;
import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("view")
public class SearchKnowledgeBean {

    private static Logger log = LogManager.getLogger(SearchKnowledgeBean.class);

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private SearchService searchService;


    private KnowledgeView view;
    private List<SelectItem> dfxCategories;
    private String selectedCategory;

    private List<KnowledgeView> results;

    @PostConstruct
    public void init() {
        log.info("Search Knowledge bean init!");
        view = new KnowledgeView();
        results = new ArrayList<>();
        dfxCategories = knowledgeService.initCategories();

    }

    public String processSearch() {
        log.info("search");
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

        results = searchService.selectSearchResultsByInput(view);


        return null;
    }

    public String processOpenKnowledge(){

        FacesContext context = FacesContext.getCurrentInstance();
        KnowledgeView view = context.getApplication().evaluateExpressionGet(context,"#{item}", KnowledgeView.class);

        knowledgeService.setCurrentKnowledge(view);

        return "knowledge?faces-redirect=true";
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

    public List<KnowledgeView> getResults() {
        return results;
    }

    public void setResults(List<KnowledgeView> results) {
        this.results = results;
    }
}
