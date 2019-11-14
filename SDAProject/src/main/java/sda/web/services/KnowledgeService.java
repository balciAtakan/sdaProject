package main.java.sda.web.services;

import main.java.sda.web.daos.KnowledgeDAO;
import main.java.sda.web.exception.SDAException;
import main.java.sda.web.util.DfXCategory;
import main.java.sda.web.util.DfXSubCategory;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("session")
public class KnowledgeService
{

    private static Logger log = LogManager.getLogger(KnowledgeService.class);

    @Autowired
    private KnowledgeDAO knowledgeDAO;


    private List<KnowledgeView> allKnowledge;
    private KnowledgeView currentKnowledge;

    private boolean isBackButtonActive = false;

    public void initAllKnowledge() throws SDAException
    {
        if (allKnowledge == null)
            allKnowledge = knowledgeDAO.getAllKnowledge();
        log.info("all knowledge has been loaded! Count : " + (allKnowledge != null ? allKnowledge.size() : " "));
    }

    public boolean saveKnowledge(KnowledgeView view) throws SDAException
    {
        view = knowledgeDAO.saveKnowledge(view);
            if(view.getSynonyms() != null && !view.getSynonyms().isEmpty())
                return knowledgeDAO.saveSynonyms(view.getSynonyms(), view.getUuid());

        return false;
    }

    public void initKnowledge()
    {
        try
        {
            List<KnowledgeView> res =  knowledgeDAO.getKnowledge(currentKnowledge);
            List<KnowledgeView> temp = new ArrayList<>();
            for (KnowledgeView element : res) {
                if (temp.isEmpty())
                    temp.add(element);
                else{
                    KnowledgeView knowledge = temp.stream().filter(a->a.getUuid().equals(element.getUuid())).findFirst().orElse(null);
                    if (knowledge == null) {
                        temp.add(element);
                    } else {
                        knowledge.getSynonyms().addAll(element.getSynonyms());
                    }
                }
            }
            currentKnowledge = res.get(0);
        } catch (SDAException e)
        {
            e.printStackTrace();
            currentKnowledge = null;
        }
    }

    public List<KnowledgeView> getKnowledgeFromWord(String word)
    {
        try
        {
            List<KnowledgeView> res =  knowledgeDAO.getKnowledgeFromWord(word);
            List<KnowledgeView> temp = new ArrayList<>();
            for (KnowledgeView element : res) {
                if (temp.isEmpty())
                    temp.add(element);
                else{
                    KnowledgeView knowledge = temp.stream().filter(a->a.getUuid().equals(element.getUuid())).findFirst().orElse(null);
                    if (knowledge == null) {
                        temp.add(element);
                    } else {
                        knowledge.getSynonyms().addAll(element.getSynonyms());
                    }
                }
            }
            return temp;
        } catch (SDAException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void updateKnowledge(KnowledgeView view) throws SDAException
    {

        knowledgeDAO.updateKnowledge(view);
    }

    public void deleteKnowledge(String uuid) throws SDAException
    {

        knowledgeDAO.deleteKnowledge(uuid);
    }

    public List<SelectItem> initCategories()
    {

        List<SelectItem> dfxCategories = new ArrayList<>();
        SelectItem[] temp = new SelectItem[]{};
        SelectItemGroup group1 = new SelectItemGroup(DfXCategory.DfA.getLongText());
        SelectItemGroup group2 = new SelectItemGroup(DfXCategory.DfC.getLongText());
        SelectItemGroup group3 = new SelectItemGroup(DfXCategory.DfE.getLongText());
        SelectItemGroup group4 = new SelectItemGroup(DfXCategory.DfM.getLongText());
        SelectItemGroup group5 = new SelectItemGroup(DfXCategory.DfMa.getLongText());
        SelectItemGroup group6 = new SelectItemGroup(DfXCategory.DfQ.getLongText());
        SelectItemGroup group7 = new SelectItemGroup(DfXCategory.DfR.getLongText());
        SelectItemGroup group8 = new SelectItemGroup(DfXCategory.DfS.getLongText());

        SelectItem group00 = new SelectItem("No SubCategory DfA");
        SelectItem group11 = new SelectItem(DfXSubCategory.SubDfA.getLongText());
        SelectItem group12 = new SelectItem(DfXSubCategory.SubDfA2.getLongText());

        SelectItem group20 = new SelectItem("No SubCategory DfC");
        SelectItem group21 = new SelectItem(DfXSubCategory.SubDfC.getLongText());

        SelectItem group30 = new SelectItem("No SubCategory DfE");
        SelectItem group31 = new SelectItem(DfXSubCategory.SubDfE.getLongText());

        SelectItem group40 = new SelectItem("No SubCategory DfM");
        SelectItem group41 = new SelectItem(DfXSubCategory.SubDfM.getLongText());

        SelectItem group50 = new SelectItem("No SubCategory DfMa");
        SelectItem group51 = new SelectItem(DfXSubCategory.SubDfMa.getLongText());

        SelectItem group60 = new SelectItem("No SubCategory DfQ");
        SelectItem group61 = new SelectItem(DfXSubCategory.SubDfQ.getLongText());

        SelectItem group70 = new SelectItem("No SubCategory DfR");
        SelectItem group71 = new SelectItem(DfXSubCategory.SubDfR.getLongText());

        SelectItem group80 = new SelectItem("No SubCategory DfS");
        SelectItem group81 = new SelectItem(DfXSubCategory.SubDfS.getLongText());

        group1.setSelectItems(new SelectItem[]{group00, group11, group12, group21});
        group2.setSelectItems(new SelectItem[]{group20, group21});
        group3.setSelectItems(new SelectItem[]{group30, group31});
        group4.setSelectItems(new SelectItem[]{group40, group41});
        group5.setSelectItems(new SelectItem[]{group50, group51});
        group6.setSelectItems(new SelectItem[]{group60, group61});
        group7.setSelectItems(new SelectItem[]{group70, group71});
        group8.setSelectItems(new SelectItem[]{group80, group81});

        dfxCategories.add(group1);
        dfxCategories.add(group2);
        dfxCategories.add(group3);
        dfxCategories.add(group4);
        dfxCategories.add(group5);
        dfxCategories.add(group6);
        dfxCategories.add(group7);
        dfxCategories.add(group8);

        return dfxCategories;
    }

    public List<KnowledgeView> getAllKnowledge()
    {
        return allKnowledge;
    }

    public void setAllKnowledge(List<KnowledgeView> allKnowledge)
    {
        this.allKnowledge = allKnowledge;
    }

    public KnowledgeView getCurrentKnowledge()
    {
        return currentKnowledge;
    }

    public void setCurrentKnowledge(KnowledgeView currentKnowledge)
    {
        this.currentKnowledge = currentKnowledge;
    }

    public boolean isBackButtonActive()
    {
        return isBackButtonActive;
    }

    public void setBackButtonActive(boolean backButtonActive)
    {
        isBackButtonActive = backButtonActive;
    }

    public void reset(){
        setAllKnowledge(null);
    }
}
