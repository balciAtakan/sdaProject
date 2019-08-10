package main.java.sda.web.backingbeans;

import main.java.sda.web.exception.SDAException;
import main.java.sda.web.services.KnowledgeRoomService;
import main.java.sda.web.services.KnowledgeService;
import main.java.sda.web.services.PersonenService;
import main.java.sda.web.services.SessionService;
import main.java.sda.web.util.*;
import main.java.sda.web.views.KnowledgeRoomMessageView;
import main.java.sda.web.views.KnowledgeRoomView;
import main.java.sda.web.views.KnowledgeView;
import main.java.sda.web.views.PersonView;
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
import java.util.*;

@Component
@Scope("view")
public class CommunicationBean implements Serializable
{

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
    private String enteredMessage;

    private KnowledgeRoomMessageView highlightedMessage;
    private boolean wordPanelOn;
    private KnowledgeView newKnowledge;
    private List<KnowledgeView> knowledgeViewListFromDB;

    private List<SelectItem> dfxCategories;
    private String selectedCategory;
    private String desc;
    private UploadedFile fileUpload;
    private InputStream stream;

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

            if (roomService.getCurrentRoom() != null) setActiveRoom(roomService.getCurrentRoom());

        } catch (SDAException e)
        {
            log.info(e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Something went wrong on Load Knowledge Rooms"));
        }

    }

    /**
     * @soundtrack
     * @since
     * @deprecated
     */
    //TODO: asny loading!!
    public void loadAsyncKnowledge()
    {

        log.info("Load async knowledge!");

        try
        {
            knowledgeService.initAllKnowledge();
        } catch (SDAException e)
        {
            log.info("Error on Load async knowledge !");
        }

    }


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
            FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a room name!", ""));
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

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, res.getMessage(), ""));
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        } catch (SDAException e)
        {

            log.info(e.getMessage());
            FacesContext.getCurrentInstance().addMessage("dialog_form:dialog_messages",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unknown error!", ""));
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
            KnowledgeRoomMessageView messageView = new KnowledgeRoomMessageView(SDAUtil.generateUuid(), enteredMessage,
                    new Date(), currUser, activeRoom.getUuid());

            messageView.copyView(processGivenMessage(messageView));

            activeRoom.getHistory().add(messageView);

            SDAResult res = roomService.saveKnowledgeRoomMessage(messageView);

            log.info(res.getMessage());

        } catch (SDAException e)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
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
    public void processEnterRoom()
    {
        //todo: find all the messages which have knowledge inside them

        if (activeRoom != null)
        {
            List<KnowledgeRoomMessageView> history = activeRoom.getHistory();
            history.sort(Comparator.comparing(KnowledgeRoomMessageView::getModifyDate));
            if (!history.isEmpty())
            {
                for (KnowledgeRoomMessageView view : history)
                {
                    view.copyView(checkWordInDB(view));
                }
            }
            setHighlightedMessage(null);

            //we set here in roomservice the active room for the back button!
            roomService.setCurrentRoom(activeRoom);
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

        if (!message.isFoundInDB()) message.copyView(checkWordInHistory(message));

        return message;
    }

    /*	This Method checks if the given MESSAGE is already in the DB so the program can show it to user. Only for the words more than 2 letter
     * 	@Usage
     * 		- Init
     * 		- Every message push into the active room
     * */
    private KnowledgeRoomMessageView checkWordInDB(KnowledgeRoomMessageView messageToCheck)
    {

        String[] allMessage = messageToCheck.getMessage().split("\\s");
        for (String message : allMessage)
        {
            if (message.length() > 2)
            {
                if (knowledgeService.getAllKnowledge().stream().anyMatch(a -> a.getWord().equalsIgnoreCase(message)))
                {
                    messageToCheck.copyView(setPrefixAndPostfixOfMessage(messageToCheck, message));
                    messageToCheck.setFoundInDB(true);
                    break;
                }
            }
        }
        return messageToCheck;
    }

    /*	This Method checks if the given MESSAGE in the room history,
     * 	If so; the word is a frequently used word, so it is possible to add new Knowledge. Only for the words more than 2 letter
     * 	@Usage
     * 		- Every message push into the active Room
     * */
    private KnowledgeRoomMessageView checkWordInHistory(KnowledgeRoomMessageView messageToCheck)
    {

        String[] word = messageToCheck.getMessage().toLowerCase().trim().split("\\s+");

        ArrayList<KnowledgeRoomMessageView> history = activeRoom.getHistory();

        boolean found = false;
        for (KnowledgeRoomMessageView hist : history)
        {
            String[] temp = hist.getMessage().toLowerCase().trim().split("\\s+");

            for (String entered : word)
                if (entered.length() > 2) for (String historied : temp)
                    if (entered.equalsIgnoreCase(historied))
                    {
                        messageToCheck.copyView(setPrefixAndPostfixOfMessage(messageToCheck, entered));
                        messageToCheck.setFoundInUsage(true);
                        return messageToCheck;
                    }
        }
        return messageToCheck;
    }

    private KnowledgeRoomMessageView setPrefixAndPostfixOfMessage(KnowledgeRoomMessageView messageToCheck, String foundMessage)
    {
        messageToCheck.setHighlightedWord(foundMessage);

        int index = messageToCheck.getMessage().indexOf(foundMessage);
        if ((index + foundMessage.length()) % messageToCheck.getMessage().length() == 0)
        {
            String[] splittedMessage = messageToCheck.getMessage().split(foundMessage);
            if (splittedMessage.length == 1)
            {
                messageToCheck.setPrefixMessage(splittedMessage[0]);
                messageToCheck.setPostfixMessage("");
            }
        } else if (index == 0 && foundMessage.length() == messageToCheck.getMessage().length())
        {
            messageToCheck.setPrefixMessage("");
            messageToCheck.setPostfixMessage("");
        } else
        {
            String[] splittedMessage = messageToCheck.getMessage().split(foundMessage);
            messageToCheck.setPrefixMessage(splittedMessage[0]);
            messageToCheck.setPostfixMessage(splittedMessage[1]);
        }
        return messageToCheck;
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

    public void addKnowledge()
    {
        if (selectedCategory == null || selectedCategory.isEmpty())
        {
            FacesContext context = FacesContext.getCurrentInstance();
            KnowledgeRoomMessageView view = context.getApplication().evaluateExpressionGet(context, "#{mes}",
                    KnowledgeRoomMessageView.class);

            getKnowledgeListFromWord(view.getHighlightedWord());

            PrimeFaces.current().scrollTo("wordPanel:wordUnit");

            return;
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

        newKnowledge.setWord(highlightedMessage.getHighlightedWord());
        newKnowledge.setOwnerID(currUser.getUuid());
        if (stream != null) newKnowledge.setFileUpload(stream);

        try
        {
            if (knowledgeService.saveKnowledge(newKnowledge)) FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Knowledge successfuly saved!", ""));
			/*
			*
			* //execute javascript oncomplete
        PrimeFaces.current().executeScript("PrimeFaces.info('Hello from the Backing Bean');");

        //update panel
        PrimeFaces.current().ajax().update("form:panel");

        //scroll to panel
        PrimeFaces.current().scrollTo("form:panel");
			*
			* */

            //execute javascript oncomplete
            PrimeFaces.current().executeScript("PF('knowledgeDlg').hide();");

            //update panel
            PrimeFaces.current().ajax().update("add_knowledge_form:panelTest");
            //PrimeFaces.current().ajax().update("wordPanel:wordUnit");
            //PrimeFaces.current().ajax().update("roomPanel:roomUnit");

        } catch (SDAException e)
        {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
            e.printStackTrace();
            log.info(e.getMessage());
        }

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
        if (newRoom == null) return new KnowledgeRoomView();
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

    public KnowledgeRoomMessageView getHighlightedMessage()
    {
        return highlightedMessage;
    }

    public void setHighlightedMessage(KnowledgeRoomMessageView highlightedMessage)
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
