package sda.web.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import sda.web.daos.DfxDAO;
import sda.web.views.DfxView;

@Service
@Scope("session")
public class SessionService {
	
	@Autowired
	private DfxDAO dfxDAO;
	
	private List<DfxView> dfxCategories;
	
	public void getCategories() {
		
		dfxCategories = dfxDAO.findAll();
	}

	public List<DfxView> initDfxCategories() {
		return dfxCategories;
	}

	public void setDfxCategories(List<DfxView> dfxCategories) {
		this.dfxCategories = dfxCategories;
	}
}
