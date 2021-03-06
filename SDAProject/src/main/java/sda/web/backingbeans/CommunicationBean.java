package main.java.sda.web.backingbeans;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.APIService;
import main.java.sda.web.services.KnowledgeRoomService;
import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.util.*;
import main.java.sda.web.views.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope("view")
public class CommunicationBean implements Serializable {

    private static Logger log = LogManager.getLogger(CommunicationBean.class);

    private static final long serialVersionUID = 1L;

    @Autowired
    private PersonenService personenService;

    @Autowired
    private KnowledgeRoomService roomService;

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private APIService apiService;

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
    private String enteredMessage;

    private MessageView highlightedMessage;
    private boolean wordPanelOn;
    private KnowledgeView newKnowledge;
    private List<KnowledgeView> knowledgeViewListFromDB;

    private List<SelectItem> dfxCategories;
    private String selectedCategory;
    private String desc;
    private UploadedFile fileUpload;
    private InputStream stream;

    private List<KnowledgeView> uniqueKnowledgeList;

    @PostConstruct
    public void init()
    {
        log.info("Communication bean init!");

        dfxCategories = knowledgeService.initCategories();
        //knowledgeViewListFromDB = new ArrayList<>();

        roles = new ArrayList<>();
        addRoom();

        try
        {
            //load currentUser from processor
            setCurrUser(personenService.getCurrentPersonDaten(personenService.getCurrUser().getUuid()));

            //load all rooms
            rooms = roomService.getKnowledgeRooms();
            knowledgeService.initAllKnowledge();

            if (roomService.getCurrentRoom() != null)
            {
                setActiveRoom(roomService.getCurrentRoom());
                processEnterRoom(false);
            }

        } catch (SDAException e)
        {
            log.info(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong on Load Knowledge Rooms"));
        }

    }

    /**
     *
     */
    //TODO: asny loading!!
/*    public void loadAsyncKnowledge()
    {

        log.info("Load async knowledge!");

        try
        {
            knowledgeService.initAllKnowledge();
        } catch (SDAException e)
        {
            log.info("Error on Load async knowledge !");
        }

    }*/
    private void addRoom()
    {
        UserRole[] roles = UserRole.values();
        //log.info("name: "+userRole.name() + " role: "+userRole.role());
        this.roles.addAll(Arrays.asList(roles));
        newRoom = new KnowledgeRoomView();
        newKnowledge = new KnowledgeView();
    }

    public String processCreateRoom() throws IOException
    {
        if (newRoom.getRoomname() != null && newRoom.getRoomname().isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a room name!", ""));
            return null;
        }

        if (selectedRoles.isEmpty())
        {
            FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",
                                                         new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please chose atleast one allowed Userrole!", ""));
            return null;
        }
        log.info("create room");

        //new room initial
        newRoom.setAllowedRoles(selectedRoles);
        newRoom.setRoomOwner(currUser.getUuid());
        newRoom.getUsers().add(currUser);
        newRoom.setNewUser(currUser);

        //selectedRoles.forEach(System.out::println);

        try
        {
            SDAResult res = roomService.saveKnowledgeRoom(newRoom);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, res.getMessage(), ""));
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (SDAException e)
        {

            log.info(e.getMessage());
            FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown error!", ""));
            return null;
        }
        return null;

    }

    public void loadRoomData()
    {
        try
        {
            activeRoom.setHistory(roomService.getKnowledgeRoomData(activeRoom.getUuid()));
        } catch (SDAException e)
        {
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }

    public void processMessage()
    {
        try
        {
            KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView(SDAUtil.generateUuid(), enteredMessage, new Date(), currUser, activeRoom.getUuid());

            for (MessageView word : messageView.getWords())
            {
                if (checkWordUseful(word.getWord()))
                {
                    word.copyView(apiService.findSynonymsWithDataMuse(SDAUtil.trimStringFormCharacters(word.getWord())));
                }
            }
            messageView.copyView(processGivenMessage(messageView));

            activeRoom.getHistory().add(0, messageView);

            SDAResult res = roomService.saveKnowledgeRoomMessage(messageView);

            //todo: check if any knowledge since last message have been added into system
            int knowledgeSize = knowledgeService.getAllKnowledge().size();
            int knowledgeSizeControl = knowledgeService.getKnowledgeCount();

            if (knowledgeSize != knowledgeSizeControl)
            {
                log.info(
                        "change method! " + LocalDateTime.now() + " username: " + currUser.getUsername() + " knowledge size: " + knowledgeService.getAllKnowledge().size());
                knowledgeService.reset();
                knowledgeService.initAllKnowledge();
                processEnterRoom(false);
            }

            log.info(res.getMessage());

        } catch (SDAException e)
        {
            log.error(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Something went wrong during sending your message!", ""));
            e.printStackTrace();

        }
    }

    ////////////////////////////////////////////////
    //											 //
    //											 //
    //				Wording Process				 //
    //											 //
    // 											 //
    ///////////////////////////////////////////////
    public void processEnterRoom(boolean controlUsage)
    {
        if (activeRoom != null)
        {
            uniqueKnowledgeList = new ArrayList<>();
            List<KnowledgeRoomMessageView> history = activeRoom.getHistory();
            history.sort(Comparator.comparing(KnowledgeRoomMessageView::getMessageDate).reversed());
            if (!history.isEmpty())
            {
                for (KnowledgeRoomMessageView view : history)
                    view.copyView(checkWordInDB(view));
            }
            setHighlightedMessage(null);

            //we set here in roomservice the active room for the back button!
            roomService.setCurrentRoom(activeRoom);


            int usageControlCounter = 0;
            //only the firs found keywords highlight!!!
            for (KnowledgeRoomMessageView view : history)
            {
                for (MessageView word : view.getWords())
                {
                    if (word.isFoundInDB())
                    {
                        if (uniqueKnowledgeList.stream().noneMatch(a -> a.getWord().equalsIgnoreCase(word.getWord())))
                        {
                            KnowledgeView knowledge = knowledgeService.getAllKnowledge().stream().filter(
                                    a -> a.getWord().equalsIgnoreCase(word.getWord())).findFirst().orElse(null);
                            if (knowledge != null)
                            {
                                uniqueKnowledgeList.add(knowledge);
                            }
                            else{
                                KnowledgeView knowledgeSynonym =new KnowledgeView(word.getWord());
                                uniqueKnowledgeList.add(knowledgeSynonym);
                            }

                        } else
                        {
                            word.setFoundInDB(false);
                        }
                    }
                    else if(controlUsage)
                    {
                        if (checkWordUseful(word.getWord()) && usageControlCounter < 4)
                        {
                            word.setFoundInUsage(checkWordInHistory(word));
                            usageControlCounter++;
                        }
                    }
                }
            }
        }
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

    private KnowledgeRoomMessageView processGivenMessage(KnowledgeRoomMessageView message)
    {
        message.copyView(checkWordInDB(message));

        for (MessageView word : message.getWords())
        {
            if (!word.isFoundInDB() && checkWordUseful(word.getWord()))
            {
                word.setFoundInUsage(checkWordInHistory(word));
            }
        }

        return message;
    }

    public void getActiveRoomMessages()
    {

        log.info(
                "getQueMEssage Method! " + LocalDateTime.now() + " username: " + currUser.getUsername() + " knowledge size: " + knowledgeService.getAllKnowledge().size());
        int messageCount = activeRoom.getHistory().size();

        try
        {
            if (messageCount != roomService.getKnowledgeRoomMessageCount(activeRoom.getUuid()))
            {
                knowledgeService.reset();
                knowledgeService.initAllKnowledge();
                loadRoomData();
                processEnterRoom(true);
                PrimeFaces.current().ajax().update("roomPanel");
            }


        } catch (Exception e)
        {
            log.error("Fail here!");
            e.printStackTrace();
        }
    }

    /*	This Method checks if the given MESSAGE is already in the DB so the program can show it to user.
     *  Only for the words more than 2 letter and for the words that are not in stopwords array
     * 	@Usage
     * 		- Init
     * 		- Every message push into the active room
     * */
    private KnowledgeRoomMessageView checkWordInDB(KnowledgeRoomMessageView messageToCheck)
    {
        List<MessageView> allMessage = messageToCheck.getWords();
        for (MessageView message : allMessage)
        {
            if (checkWordUseful(message.getWord()))
            {

                if (knowledgeService.getAllKnowledge().stream().anyMatch(a -> a.getWord().equalsIgnoreCase(message.getWord())))
                {
                    message.setFoundInDB(true);
                    return messageToCheck;
                }
                for (KnowledgeView view : knowledgeService.getAllKnowledge())
                {
                    WordView looked = view.getSynonyms().stream().filter(b -> b.getWord().equalsIgnoreCase(message.getWord())).findFirst().orElse(null);
                    if (looked != null)
                    {
                        message.setOrigin(view.getWord());
                        message.setFoundInDB(true);
                        return messageToCheck;
                    }
                }
            }
        }
        return messageToCheck;
    }

    private List<KnowledgeRoomMessageView> getHistoryRule(List<KnowledgeRoomMessageView> history)
    {
        return history.stream().limit(SDAConstants.HISTORY_LIMIT).collect(Collectors.toList());
    }

    private boolean checkWordUseful(String word)
    {
        return word.length() > 2 && !SDAConstants.getStopwordsMoreThan2Digits().contains(word.toLowerCase());
    }

    /*	This Method checks if the given MESSAGE in the room history,
     * 	If so; the word is a frequently used word, so it is possible to add new Knowledge. Only for the words more than 2 letter
     * 	@Usage
     * 		- Every message push into the active Room
     * */
    private boolean checkWordInHistory(MessageView messageToCheck)
    {
        String givenWord = messageToCheck.getWord().toLowerCase();

        List<KnowledgeRoomMessageView> history = activeRoom.getHistory();

        history = getHistoryRule(history);

        for (KnowledgeRoomMessageView hist : history)
        {
            for (MessageView word : hist.getWords())
            {
                if (givenWord.equalsIgnoreCase(word.getWord()) || word.getSynonyms().stream().anyMatch(a -> a.getWord().equalsIgnoreCase(word.getWord())))
                {
                    return true;
                }
            }
        }
        return false;
    }

    ////////////////////////////////////////////////
    //											 //
    //											 //
    //				Knowledge Process			 //
    //											 //
    // 											 //
    ///////////////////////////////////////////////

    public void initNewRoom()
    {
        newKnowledge = new KnowledgeView();
    }

    public String addKnowledge()
    {
        if (selectedCategory == null || selectedCategory.isEmpty())
        {
            FacesContext context = FacesContext.getCurrentInstance();
            MessageView view = context.getApplication().evaluateExpressionGet(context, "#{word}", MessageView.class);

            getKnowledgeListFromWord(view.getOrigin() != null ? view.getOrigin() : view.getWord());

            PrimeFaces.current().scrollTo("wordPanel:wordUnit");

            return null;
        }
        if (selectedCategory.contains("SubCategory"))
        {
            log.info("chosen no sub category: " + selectedCategory.substring(15));
            newKnowledge.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(15)));
        } else
        {
            log.info("chosen category: " + selectedCategory);
            newKnowledge.setDfXCategory(DfXCategory.getEnum(selectedCategory.substring(0, 4).trim()));
            newKnowledge.setDfXSubCategory(DfXSubCategory.getEnum(selectedCategory, true));
        }

        newKnowledge.setWord(highlightedMessage.getWord());
        newKnowledge.setOwnerID(currUser.getUuid());
        newKnowledge.setSynonyms(highlightedMessage.getSynonyms());
        if (stream != null)
        {
            newKnowledge.setFileUpload(stream);
        }

        try
        {
            if (knowledgeService.saveKnowledge(newKnowledge))
            {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Knowledge successfuly saved!", ""));

                knowledgeService.reset();
                return "communication?faces-redirect=true";
            }


            //PrimeFaces.current().ajax().update("wordPanel:wordUnit");
            //PrimeFaces.current().ajax().update("roomPanel:roomUnit");

        } catch (SDAException e)
        {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
            e.printStackTrace();
            log.info(e.getMessage());
        }
        //execute javascript oncomplete
        PrimeFaces.current().executeScript("PF('knowledgeDlg').hide();");

        //update panel
        PrimeFaces.current().ajax().update("add_knowledge_form:panelTest");
        return null;

    }

    private void getKnowledgeListFromWord(String word)
    {
        setKnowledgeViewListFromDB(knowledgeService.getKnowledgeFromWord(word));
    }

    public String processOpenKnowledge()
    {

        FacesContext context = FacesContext.getCurrentInstance();
        KnowledgeView view = context.getApplication().evaluateExpressionGet(context, "#{value}", KnowledgeView.class);

        knowledgeService.setCurrentKnowledge(view);

        return "knowledge?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event)
    {
        log.info("file gogogo");
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        fileUpload = event.getFile();
        try
        {
            stream = fileUpload.getInputstream();
            log.info("file upload successful");
        } catch (IOException e)
        {
            e.printStackTrace();
            log.info("file upload failed");
        }
    }

    ////////////////////////////////////////////////
    //											 //
    //											 //
    //				Getter Setters				 //
    //											 //
    // 											 //
    ///////////////////////////////////////////////
    public void setBackButtonActive(boolean value)
    {
        knowledgeService.setBackButtonActive(value);
    }

    public int getTotalRooms()
    {
        return rooms == null ? 0 : rooms.size();
    }

    public KnowledgeRoomView getNewRoom()
    {
        if (newRoom == null)
        {
            return new KnowledgeRoomView();
        }
        return newRoom;
    }

    public List<UserRole> getSelectedRoles()
    {
        return selectedRoles;
    }

    public void setSelectedRoles(List<UserRole> selectedRoles)
    {
        this.selectedRoles = selectedRoles;
    }

    public List<UserRole> getRoles()
    {
        return roles;
    }

    public void setRoles(List<UserRole> roles)
    {
        this.roles = roles;
    }

    public PersonView getCurrUser()
    {
        return currUser;
    }

    public void setCurrUser(PersonView currUser)
    {
        this.currUser = currUser;
    }

    public ArrayList<KnowledgeRoomView> getRooms()
    {
        return rooms;
    }

    public void setRooms(ArrayList<KnowledgeRoomView> rooms)
    {
        this.rooms = rooms;
    }

    public void setNewRoom(KnowledgeRoomView newRoom)
    {
        this.newRoom = newRoom;
    }

    public KnowledgeRoomView getActiveRoom()
    {
        return activeRoom;
    }

    public void setActiveRoom(KnowledgeRoomView activeRoom)
    {
        this.activeRoom = activeRoom;
    }

    public String getEnteredMessage()
    {
        return enteredMessage;
    }

    public void setEnteredMessage(String enteredMessage)
    {
        this.enteredMessage = enteredMessage;
    }

    public MessageView getHighlightedMessage()
    {
        return highlightedMessage;
    }

    public void setHighlightedMessage(MessageView highlightedMessage)
    {
        this.highlightedMessage = highlightedMessage;
    }

    public boolean isWordPanelOn()
    {
        return wordPanelOn;
    }

    public void setWordPanelOn(boolean wordPanelOn)
    {
        this.wordPanelOn = wordPanelOn;
    }

    public KnowledgeView getNewKnowledge()
    {
        return newKnowledge;
    }

    public void setNewKnowledge(KnowledgeView newKnowledge)
    {
        this.newKnowledge = newKnowledge;
    }

    public List<SelectItem> getDfxCategories()
    {
        return dfxCategories;
    }

    public void setDfxCategories(List<SelectItem> dfxCategories)
    {
        this.dfxCategories = dfxCategories;
    }

    public String getSelectedCategory()
    {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory)
    {
        this.selectedCategory = selectedCategory;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public List<KnowledgeView> getKnowledgeViewListFromDB()
    {
        return knowledgeViewListFromDB;
    }

    public void setKnowledgeViewListFromDB(List<KnowledgeView> knowledgeViewListFromDB)
    {
        this.knowledgeViewListFromDB = knowledgeViewListFromDB;
    }

    public UploadedFile getFileUpload()
    {
        return fileUpload;
    }

    public void setFileUpload(UploadedFile fileUpload)
    {
        this.fileUpload = fileUpload;
    }
}
