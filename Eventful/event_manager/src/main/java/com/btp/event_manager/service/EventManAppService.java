package com.btp.event_manager.service;

import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.model.BaseEvent;
import com.btp.appfx.model.User;
import com.btp.appfx.service.AppService;
import com.btp.appfx.service.CipherService;
import com.btp.budget.model.BudgetTracker;
import com.btp.event_manager.component.*;
import com.btp.event_manager.model.EventManState;
import com.btp.login.components.LoginUI;
import com.btp.login.service.RemoveUserService;
import com.btp.logs.service.LogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventManAppService implements AppService, LogService {
    private LoginUI loginUI;
    private CreateEventUI createEventUI;
    private BudgetTrackerUI budgetTrackerUI;
    private DashboardUI dashboardUI;
    private EventDetailsUI eventDetailsUI;
    private EventTimelineUI eventTimelineUI;
    private LogsUI logsUI;
    private MyEventUI myEventUI;
    private EventManState eventManState;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm:ss");

    public EventManState getEventManState() {
        return eventManState;
    }

    public EventManAppService(EventManState eventManState) { this.eventManState = eventManState;}

    @Override
    public void save(String filename) {

    }

    @Override
    public void exit() {

    }

    @Override
    public void close() {

    }

    @Override
    public void login(User user) {
        eventManState.setCurrUser(user);
        LoadUserEvents.load(this);
        _loggedIn();
        _eventsLoaded();
    }

    @Override
    public void logout() {
        loginUI.start(getMainStage());
        _loggedOut();

        Stage temp = getMainStage();
        eventManState = new EventManState();
        setMainStage(temp);
    }

    @Override
    public void createAccount(String user, String pass, String passConfirm) {
    }

    @Override
    public void deleteAccount() {
        if (RemoveUserService.delete(this)) {
            loginUI.start(getMainStage());
            Stage temp = getMainStage();
            eventManState = new EventManState();
            setMainStage(temp);
        }
    }

    @Override
    public void createEvent(BaseEvent baseEvent) {
            baseEvent.setLastAccessed(getSysDateTime());
            eventManState.getCurrUser().getEvents().add(baseEvent);
            setSelectedEvent(baseEvent);
            setBudgetTracker(null);
            _createdEvent();
            setSaveStatus(SaveStatus.SAVED);
    }

    @Override
    public void updateEvent(EventFormEvents update) {
        if (update.equals(EventFormEvents.UPDATE_BUDGET)) {
            WriteEventsService.overwrite(this, eventManState.getStartDateBuffer(), eventManState.getEndDateBuffer());
            LoadUserEvents.load(this);
            setSaveStatus(SaveStatus.SAVED);
            _savedEvent();
            _budgetTrackerUpdated();
            _eventsLoaded();
        }
    }

    @Override
    public void updateEvent(EventFormEvents update, String input) {
        switch (update) {
            case DESC:
                if (input != null)
                    input = input.strip();

                if (input == getDescription()) {
                    return;
                }

                eventManState.setDescriptionBuffer(input);
                setSaveStatus(SaveStatus.UNSAVED);
                getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
                break;
            case UPDATE_CHANGES:
                if (!RegexService.validate(input)){
                    setGuests(new ArrayList<>());
                } else {
                    setGuests(List.of(input.split(";")));
                }

                if (getStartDate() != eventManState.getStartDateBuffer()) {
                    _startDateUpdated();
                }

                if (getEndDate() != eventManState.getEndDateBuffer()) {
                    _endDateUpdated();
                }

                if (getDescription() != eventManState.getDescriptionBuffer() && eventManState.getDescriptionBuffer() != null) {
                    setDescription(eventManState.getDescriptionBuffer());
                    _descriptionUpdated();
                }

                if (getStartTime() != eventManState.getStartTimeBuffer() && eventManState.getStartTimeBuffer() != null) {
                    setStartTime(eventManState.getStartTimeBuffer());
                    _startTimeUpdated();
                }

                if (getEndTime() != eventManState.getEndTimeBuffer() && eventManState.getEndTimeBuffer() != null) {
                    setEndTime(eventManState.getEndTimeBuffer());
                    _endTimeUpdated();
                }

                WriteEventsService.overwrite(this, eventManState.getStartDateBuffer(), eventManState.getEndDateBuffer());
                LoadUserEvents.load(this);
                setSaveStatus(SaveStatus.SAVED);
                _savedEvent();
                _eventsLoaded();

                eventManState.setStartDateBuffer(null);
                eventManState.setEndDateBuffer(null);
                eventManState.setDescriptionBuffer(null);
                eventManState.setStartTimeBuffer(null);
                eventManState.setEndTimeBuffer(null);

                break;
        }
    }

    @Override
    public void updateEvent(EventFormEvents update, LocalDate input) {
        switch (update) {
            case START_DATE:
                if (getStartDate() == input) {
                    return;
                }
                eventManState.setStartDateBuffer(input);
                break;
            case END_DATE:
                if (getEndDate() == input) {
                    return;
                }
                eventManState.setEndDateBuffer(input);
                break;
        }
        setSaveStatus(SaveStatus.UNSAVED);
    }

    @Override
    public void updateEvent(EventFormEvents update, LocalTime input) {
        switch (update) {
            case START_TIME:
                if (getStartTime() == input) {
                    return;
                }
                eventManState.setStartTimeBuffer(input);
                break;
            case END_TIME:
                if (getEndTime() == input) {
                    return;
                }
                eventManState.setEndTimeBuffer(input);
                break;
        }
        setSaveStatus(SaveStatus.UNSAVED);
    }

    @Override
    public void removeEvent() {
        if (WriteEventsService.delete(this)) {
            for (BaseEvent event : eventManState.getCurrUser().getEvents()) {
                if (event == getSelectedEvent()) {
                    eventManState.getCurrUser().getEvents().remove(event);
                    _deletedEvent();
                    break;
                }
            }
            dashboardUI.start(getMainStage());
        }
    }

    @Override
    public BaseEvent getSelectedEvent() {
        return eventManState.getCurrSelectedEvent();
    }

    @Override
    public void setSelectedEvent(BaseEvent baseEvent) {
        eventManState.setCurrSelectedEvent(baseEvent);
        _accessedEvent();
    }

    @Override
    public String getEventName() {
        return eventManState.getCurrSelectedEvent().getEventName();
    }

    @Override
    public void setEventName(String eventName) {
        eventManState.getCurrSelectedEvent().setEventName(eventName);
    }

    @Override
    public LocalDate getStartDate() {
        return eventManState.getCurrSelectedEvent().getStartDate();
    }

    @Override
    public void setStartDate(LocalDate date) {
        eventManState.getCurrSelectedEvent().setStartDate(date);
    }

    @Override
    public LocalDate getEndDate() {
        return eventManState.getCurrSelectedEvent().getEndDate();
    }

    @Override
    public void setEndDate(LocalDate date) {
        eventManState.getCurrSelectedEvent().setEndDate(date);
    }

    @Override
    public LocalTime getStartTime() {
        return eventManState.getCurrSelectedEvent().getStartTime();
    }

    @Override
    public void setStartTime(LocalTime time) {
        eventManState.getCurrSelectedEvent().setStartTime(time);
    }

    @Override
    public LocalTime getEndTime() {
        return eventManState.getCurrSelectedEvent().getEndTime();
    }

    @Override
    public void setEndTime(LocalTime time) {
        eventManState.getCurrSelectedEvent().setEndTime(time);
    }

    @Override
    public String getDescription() {
        return eventManState.getCurrSelectedEvent().getDescription();
    }

    @Override
    public void setDescription(String description) {
        eventManState.getCurrSelectedEvent().setDescription(description);
    }

    @Override
    public List<String> getGuests() {
        return eventManState.getCurrSelectedEvent().getGuests();
    }

    @Override
    public void setGuests(List<String> guests) {
        eventManState.getCurrSelectedEvent().setGuests(guests);
    }

    @Override
    public void addGuest(String guest) {
        eventManState.getCurrSelectedEvent().getGuests().add(guest);
    }

    @Override
    public void removeGuest(String guest) {
        eventManState.getCurrSelectedEvent().getGuests().remove(guest);
    }

    @Override
    public void inviteGuests(String guests){
        if(GoogleCalendar.validMailArea(guests, this)) {
            Task<Void> invitationTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try {
                        GoogleCalendar.sendEventInvitation(EventManAppService.this);
                    } catch (GeneralSecurityException | IOException e) {
                        _updateLogs("Failed to send invitations: " + e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Send Invitation");
                    alert.setHeaderText("Invitation Sent Successfully!");
                    alert.setContentText("The invitation was sent to the guest(s). Any confirmation or declination from guests will be sent to the sender's email account.");
                    alert.showAndWait();
                    _guestsInvited();
                }

                @Override
                protected void failed() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Send Invitation");
                    alert.setHeaderText("Invitation Sent Unsuccessfully!");
                    alert.setContentText("Something went wrong and the invitation was not sent to the guest(s). Please try again later.");
                    alert.showAndWait();
                }
            };

            new Thread(invitationTask).start();
        }
    }

    public BudgetTracker getBudgetTracker() {
        return eventManState.getCurrBudgetTracker();
    }

    public void setBudgetTracker(BudgetTracker budgetTracker) {
        eventManState.setCurrBudgetTracker(budgetTracker);
    }

    @Override
    public LocalDateTime getLastAccessed() {
        return eventManState.getCurrSelectedEvent().getLastAccessed();
    }

    @Override
    public void setLastAccessed(LocalDateTime dateTime) {
        eventManState.getCurrSelectedEvent().setLastAccessed(dateTime);
    }

    @Override
    public boolean isLoggedIn() {
        return eventManState.isLoggedIn();
    }

    @Override
    public void setLogIn(boolean loggedIn) {
        eventManState.setLoggedIn(loggedIn);
    }

    @Override
    public LocalDateTime getSysDateTime() {
        return eventManState.getCurrDateTime();
    }

    @Override
    public void setSysDateTime(LocalDateTime localDateTime) {
        eventManState.setCurrDateTime(localDateTime);
    }

    @Override
    public User getCurrUser() {
        return eventManState.getCurrUser();
    }

    @Override
    public void setCurrUser(User user) {
        eventManState.setCurrUser(user);
    }

    @Override
    public SaveStatus getSaveStatus() {
        return eventManState.getSaveStatus();
    }

    @Override
    public void setSaveStatus(SaveStatus saveStatus) {
        eventManState.setSaveStatus(saveStatus);
    }

    @Override
    public File getMetaData() {
       return eventManState.getMetadata();
    }

    @Override
    public void verifyMetaData() throws Exception {
        String path  = "Eventful - Event Management System/";
        File metadata = new File(path + "metadata.json");
        if (metadata.exists() && !metadata.isDirectory()) {
            setMetaData(metadata);
        } else {
            File eventfulDir = new File(path);
            if (!eventfulDir.exists()) {
                eventfulDir.mkdirs(); // Create the directory if it does not exist
            }

            // Now create the metadata file
            try {
                if (metadata.createNewFile()) {
                    // If the file was created successfully, initialize it
                    initMetaData(metadata);
                    setMetaData(metadata);
                    System.out.println("Metadata file created.");
                } else {
                    System.out.println("Metadata file already exists.");
                }
            } catch (IOException e) {
                System.out.println("Error creating metadata file: " + e.getMessage());
            }
        }
    }

    @Override
    public void initMetaData(File metadata) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        jsonNode.put("app_name", "eventful");
        jsonNode.put("version", 2.0);
        jsonNode.put("directory", metadata.getAbsolutePath());
        jsonNode.put("last_backup", getSysDateTime().toString());
        jsonNode.put("next_backup", getSysDateTime().plusHours(12).toString());
        objectMapper.writeValue(metadata, jsonNode);
    }

    @Override
    public void setMetaData(File metaData) {
        eventManState.setMetadata(metaData);
    }

    @Override
    public String getDirectory() {
        return eventManState.getDirectory();
    }

    @Override
    public void setDirectory(String directory) {
        eventManState.setDirectory(directory);
    }

    @Override
    public String getEmailAdd() {
        return eventManState.getEmailAdd();
    }

    @Override
    public void setEmailAdd(String emailAdd) {
        eventManState.setEmailAdd(emailAdd);
    }

    @Override
    public String getEmailPass() {
        return eventManState.getEmailPass();
    }

    @Override
    public void setEmailPass(String emailPass) {
        eventManState.setEmailPass(emailPass);
    }

    public void setMainStage(Stage stage) {
        eventManState.setMainStage(stage);
    }

    public Stage getMainStage() {
        return eventManState.getMainStage();
    }

    public void setLoginUI(LoginUI loginUI) {
        this.loginUI = loginUI;
    }

    // methods prefixed with '_' are for logging purposes

    @Override
    public void _updateLogs(String text) {
        Path directoryPath = Paths.get("Eventful - Event Management System/dat/" + getCurrUser().getUsername());
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Eventful - Event Management System/dat/" + getCurrUser().getUsername() + "/logs.txt", true))) {
            String entry = "(" + getCurrUser().getUsername() + ") [" + getSysDateTime().format(formatter) + "]: " + text;
            writer.write(CipherService.encrypt(entry));
            writer.newLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String _loadLogs() {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("Eventful - Event Management System/dat/" + getCurrUser().getUsername() + "/logs.txt"))) {
            String line;
            List<String> entries = new ArrayList<>();
            while((line = reader.readLine() ) != null) {
                entries.add(line);
            }

            Collections.reverse(entries);
            for (String entry: entries) {
                content.append(CipherService.decrypt(entry)).append("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    @Override
    public void _loggedIn() {
        _updateLogs("Successfully logged in into this user account.");
    }

    @Override
    public void _loggedOut() {
        _updateLogs("Successfully logged out from this user account.");
    }

    @Override
    public void _eventsLoaded() {
        _updateLogs("Loaded a total of " + eventManState.getCurrUser().getEvents().size() + " user created events.");
    }

    @Override
    public void _accessedEvent() {
        _updateLogs("Accessed event details of " + getSelectedEvent().getEventName() + ".");
    }

    @Override
    public void _createdEvent() {
        _updateLogs("Created an event named, " + getEventName() + ", scheduled from " +
                    getSelectedEvent().getStartDate() + " to " + getSelectedEvent().getEndDate() + ".");
    }

    @Override
    public void _savedEvent() {
        _updateLogs("Overwrote event details in " + getSelectedEvent().getEventName());
    }

    @Override
    public void _deletedEvent() {
        _updateLogs("Deleted an event name, " + getEventName());
    }

    @Override
    public void _startDateUpdated() {
        _updateLogs("Changed start date of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getStartDate() + ".");
    }

    @Override
    public void _endDateUpdated() {
        _updateLogs("Changed end date of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getEndDate() + ".");
    }

    @Override
    public void _startTimeUpdated() {
        _updateLogs("Changed start time of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getStartTime() + ".");
    }

    @Override
    public void _endTimeUpdated() {
        _updateLogs("Changed end time of " + getSelectedEvent().getEventName() + " to " + getSelectedEvent().getEndTime() + ".");
    }

    @Override
    public void _descriptionUpdated() {
        _updateLogs("Changed description of " + getSelectedEvent().getEventName() + " to \"" + getSelectedEvent().getDescription() + "\".");
    }

    @Override
    public void _guestsInvited() {
        _updateLogs("Sent an invitation email to the following addresses: " + String.join(";", getGuests()) + ".");
    }

    @Override
    public void _budgetTrackerUpdated() {
        _updateLogs("Overwrote expenses details in " + getSelectedEvent().getEventName());
    }

    @Override
    public void _printedTimeline() {

    }

    public LoginUI getLoginUI() {
        return loginUI;
    }

    public CreateEventUI getCreateEventUI() {
        return createEventUI;
    }

    public void setCreateEventUI(CreateEventUI createEventUI) {
        this.createEventUI = createEventUI;
    }

    public BudgetTrackerUI getBudgetTrackerUI() {
        return budgetTrackerUI;
    }

    public void setBudgetTrackerUI(BudgetTrackerUI budgetTrackerUI) {
        this.budgetTrackerUI = budgetTrackerUI;
    }

    public DashboardUI getDashboardUI() {
        return dashboardUI;
    }

    public void setDashboardUI(DashboardUI dashboardUI) {
        this.dashboardUI = dashboardUI;
    }

    public EventDetailsUI getEventDetailsUI() {
        return eventDetailsUI;
    }

    public void setEventDetailsUI(EventDetailsUI eventDetailsUI) {
        this.eventDetailsUI = eventDetailsUI;
    }

    public EventTimelineUI getEventTimelineUI() {
        return eventTimelineUI;
    }

    public void setEventTimelineUI(EventTimelineUI eventTimelineUI) {
        this.eventTimelineUI = eventTimelineUI;
    }

    public LogsUI getLogsUI() {
        return logsUI;
    }

    public void setLogsUI(LogsUI logsUI) {
        this.logsUI = logsUI;
    }

    public MyEventUI getMyEventUI() {
        return myEventUI;
    }

    public void setMyEventUI(MyEventUI myEventUI) {
        this.myEventUI = myEventUI;
    }
}
