package main.java.sda.web.backingbeans;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.servlet.http.HttpServletRequest;

import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.util.*;
import main.java.sda.web.views.KnowledgeView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeRoomService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.services.SessionService;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import main.java.sda.web.views.PersonView;

@Component
@Scope("view")
public class CommunicationBean implements Serializable{

	private static Logger log = LogManager.getLogger(CommunicationBean.class);

	private static final long serialVersionUID = 1L;

	@Autowired
	private PersonenService personenService;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private KnowledgeRoomService roomService;

	@Autowired
	private KnowledgeService knowledgeService;

	private PersonView currUser;
	private ArrayList<KnowledgeRoomView> rooms;
	
	private KnowledgeRoomView newRoom;
	private List<UserRole> roles;
	private List<UserRole> selectedRoles;
	/////////////////////////////////////
	//----------------------------------/
	//----------------------------------/
	//----------------------------------/
	/////////////////////////////////////
	private KnowledgeRoomView activeRoom;
	private String message;

	private String highlightedWord;
	private boolean wordPanelOn;
	private KnowledgeView newKnowledge;

	private List<SelectItem> dfxCategories;
	private String selectedCategory;
    private String desc;
	
	@PostConstruct
	public void init(){
		
		log.info("Communication bean init!");

        initCategories();

		roles = new ArrayList<>();
		addRoom();
		
		try {
			//load currentUser from processor
			setCurrUser(personenService.getCurrentPersonDaten(personenService.getCurrUser().getUuid()));
			//load all rooms
			rooms = roomService.getKnowledgeRooms();
		} catch (SDAException e) {
			log.info(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error!","Something went wrong on Load Knowledge Rooms"));
		}
		
	}

	public void loadAsyncKnowledge(){

		log.info("Load async knowledge!");

		try {
			knowledgeService.initAllKnowledge();
		} catch (SDAException e) {
			log.info("Error on Load async knowledge !");
		}

	}

	private void initCategories(){

        dfxCategories = new ArrayList<>();
        SelectItem[] temp = new SelectItem[]{};
        SelectItemGroup group1 = new SelectItemGroup(DfXCategory.DfA.getLongText());
        SelectItemGroup group2 = new SelectItemGroup(DfXCategory.DfC.getLongText());
        SelectItemGroup group3 = new SelectItemGroup(DfXCategory.DfE.getLongText());
        SelectItemGroup group4 = new SelectItemGroup(DfXCategory.DfM.getLongText());
        SelectItemGroup group5 = new SelectItemGroup(DfXCategory.DfMa.getLongText());
        SelectItemGroup group6 = new SelectItemGroup(DfXCategory.DfQ.getLongText());
        SelectItemGroup group7 = new SelectItemGroup(DfXCategory.DfR.getLongText());
        SelectItemGroup group8 = new SelectItemGroup(DfXCategory.DfS.getLongText());

        SelectItem group00 = new SelectItem( "No SubCategory DfA");
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

        group1.setSelectItems(new SelectItem[]{group00,group11, group12,group21});
        group2.setSelectItems(new SelectItem[]{group20,group21});
        group3.setSelectItems(new SelectItem[]{group30,group31});
        group4.setSelectItems(new SelectItem[]{group40,group41});
        group5.setSelectItems(new SelectItem[]{group50,group51});
        group6.setSelectItems(new SelectItem[]{group60,group61});
        group7.setSelectItems(new SelectItem[]{group70,group71});
        group8.setSelectItems(new SelectItem[]{group80,group81});

        dfxCategories.add(group1);
        dfxCategories.add(group2);
        dfxCategories.add(group3);
        dfxCategories.add(group4);
        dfxCategories.add(group5);
        dfxCategories.add(group6);
        dfxCategories.add(group7);
        dfxCategories.add(group8);
    }
	
	private void addRoom(){
		
		UserRole[] roles = UserRole.values();
		//log.info("name: "+userRole.name() + " role: "+userRole.role());
		this.roles.addAll(Arrays.asList(roles));
		newRoom = new KnowledgeRoomView();
		newKnowledge = new KnowledgeView();
	}
	
	public String processCreateRoom() throws IOException
	{
		if(newRoom.getRoomname() != null && newRoom.getRoomname().isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please enter a room name!",""));
			return null;
		}
		
		if(selectedRoles.isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Please chose atleast one allowed Userrole!",""));
			return null;
		}
		log.info("create room");
		
		//new room initial 
		newRoom.setAllowedRoles(selectedRoles);
		newRoom.setRoomOwner(currUser.getUuid());
		newRoom.getUsers().add(currUser);
		newRoom.setNewUser(currUser);
		
		//selectedRoles.forEach(System.out::println);
		
		try {
			
			SDAResult res = roomService.saveKnowledgeRoom(newRoom);

			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,res.getMessage(),""));
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (SDAException e) {
			
			log.info(e.getMessage());
			FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",new FacesMessage(FacesMessage.SEVERITY_ERROR,"Unknown error!",""));
			return null;
		}
	    return null;
		
	}
	
	public void processEnterRoom(){
		
		// todo!!!!
		/*
		if(activeRoom.getAllowedRoles().stream().anyMatch(role -> role.name().equals(currUser.getRole())))
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('enterRoomDlg').show();");
		}
		else
		{
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('deniedEnterRoom').show();");
		}
		*/
		
	}
	
	public void loadRoomData(ActionEvent event)
	{
		try 
		{
			activeRoom.setHistory(roomService.getKnowledgeRoomData(activeRoom.getUuid()));
		} catch (SDAException e) {
			e.printStackTrace();
			log.info(e.getMessage());
		}
		
		
	}

	public void processMessage(ActionEvent event)
	{	
		try 
		{
		
			KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView(SDAUtil.generateUuid(), message,new Date(),currUser,activeRoom.getUuid());
			
			messageView.setFound(processWords(messageView));
			
			activeRoom.getHistory().add(messageView);
		
			SDAResult res = roomService.saveKnowledgeRoomMessage(messageView);
			log.info(res.getMessage());
			
		} catch (SDAException e) {
			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			e.printStackTrace();
			log.info(e.getMessage());
		}
	}

	////////////////////////////////////////////////
	//											 //
	//											 //
	//				Wording Process				 //
	//											 //
	// 											 //
	///////////////////////////////////////////////
	private boolean processWords(KnowledgeRoomMessageView message)
	{
		
		String[] word= message.getMessage().toLowerCase().trim().split("\\s+");
		List<String> list = Arrays.stream(word).collect(Collectors.toList());
		
		ArrayList<KnowledgeRoomMessageView> history = activeRoom.getHistory();
		
		boolean found = false;
		for (KnowledgeRoomMessageView hist : history) {
			
			String[] temp = hist.getMessage().toLowerCase().trim().split("\\s+");
			List<String> list2 = Arrays.stream(temp).collect(Collectors.toList());

			for(String entered : list)
			{
				for(String historied : list2)
				{
					if(entered.equalsIgnoreCase(historied))
					{
						message.setHighlightedWord(entered);
						found = true;
						return found;
					}
				}
			}
		}
		
		return found;
	}

	////////////////////////////////////////////////
	//											 //
	//											 //
	//				Knowledge Process			 //
	//											 //
	// 											 //
	///////////////////////////////////////////////

	public void initNewRoom(){ newKnowledge = new KnowledgeView();}
	public void addKnowledge(){
	    if(selectedCategory == null)
	        return;
        if(selectedCategory.contains("SubCategory")) {
            log.info("chosen no sub category: " + selectedCategory.substring(15));
            newKnowledge.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(15)));
        }
        else
        {
            log.info("chosen category: " +selectedCategory);
            newKnowledge.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(0,4).trim()));
            newKnowledge.setDfXSubCategory(DfXSubCategory.getEnum(selectedCategory, true));
        }

        newKnowledge.setWord(highlightedWord);
        newKnowledge.setOwner(currUser.getUuid());
        try {
        	if(knowledgeService.saveKnowledge(newKnowledge))
				FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_INFO,"Knowledge successfuly saved!",""));

		}catch (SDAException e)
		{

			FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_ERROR,e.getMessage(),""));
			e.printStackTrace();
			log.info(e.getMessage());
		}

	}

	////////////////////////////////////////////////
	//											 //
	//											 //
	//				Getter Setters				 //
	//											 //
	// 											 //
	///////////////////////////////////////////////
	public int getTotalRooms(){
		return rooms == null ? Integer.valueOf(0): rooms.size();
	}

	public KnowledgeRoomView getNewRoom() {
		if(newRoom == null)
			return new KnowledgeRoomView();
		return newRoom;
	}

	public List<UserRole> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<UserRole> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public PersonView getCurrUser() {
		return currUser;
	}

	public void setCurrUser(PersonView currUser) {
		this.currUser = currUser;
	}

	public ArrayList<KnowledgeRoomView> getRooms() {
		return rooms;
	}

	public void setRooms(ArrayList<KnowledgeRoomView> rooms) {
		this.rooms = rooms;
	}

	public void setNewRoom(KnowledgeRoomView newRoom) {
		this.newRoom = newRoom;
	}

	public KnowledgeRoomView getActiveRoom() {
		return activeRoom;
	}

	public void setActiveRoom(KnowledgeRoomView activeRoom) {
		this.activeRoom = activeRoom;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHighlightedWord() {
		return highlightedWord;
	}

	public void setHighlightedWord(String highlightedWord) {
		this.highlightedWord = highlightedWord;
	}

	public boolean isWordPanelOn() {
		return wordPanelOn;
	}

	public void setWordPanelOn(boolean wordPanelOn) {
		this.wordPanelOn = wordPanelOn;
	}

	public KnowledgeView getNewKnowledge() {
		return newKnowledge;
	}

	public void setNewKnowledge(KnowledgeView newKnowledge) {
		this.newKnowledge = newKnowledge;
	}

    public List<SelectItem> getDfxCategories() {
        return dfxCategories;
    }

    public void setDfxCategories(List<SelectItem> dfxCategories) {
        this.dfxCategories = dfxCategories;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
