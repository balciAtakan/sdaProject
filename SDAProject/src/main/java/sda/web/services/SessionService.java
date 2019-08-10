package main.java.sda.web.services;

import main.java.sda.web.daos.DfxDAO;
import main.java.sda.web.views.DfxView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("session")
public class SessionService
{

    @Autowired
    private DfxDAO dfxDAO;

    private List<DfxView> dfxCategories;

    public void getCategories()
    {

        dfxCategories = dfxDAO.findAll();
    }

    public List<DfxView> initDfxCategories()
    {
        return dfxCategories;
    }

    public void setDfxCategories(List<DfxView> dfxCategories)
    {
        this.dfxCategories = dfxCategories;
    }
}
