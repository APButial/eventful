package com.btp.event_manager.component;
import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.service.AppDataPath;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.Event;
import com.btp.event_manager.service.*;
import com.btp.event_manager.model.EventManState;
import com.btp.login.components.LoginUI;
import com.btp.login.service.LoginSuccessListener;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainFrame extends Application {
    private AppService appService;
    private EventManState eventManState;
    private LoginUI loginUI;
    private DashboardUI dashboardUI;
    private CreateEventUI createEventUI;
    private EventDetailsUI eventDetailsUI;
    private MyEventUI myEventUI;
    private EventTimelineUI eventTimelineUI;
    private LogsUI logsUI;
    private BudgetTrackerUI budgetTrackerUI;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        eventManState = new EventManState();
        appService = new EventManAppService(eventManState);
        ((EventManAppService) appService).setMainStage(primaryStage);

        appService.verifyMetaData();
        appService.generateBackup(true);

        Image logoImg = new Image(getClass().getResourceAsStream("/logo.png"));
        primaryStage.getIcons().add(logoImg);

        loginUI = new LoginUI(appService, new LoginSuccessListener() {
            @Override
            public void onLoginSuccess() {
                ((EventManAppService) appService).getDashboardUI().start(primaryStage);
            }

            @Override
            public void returnLogin() throws Exception {
                ((EventManAppService) appService).getLoginUI().start(primaryStage);
            }
        });
        ((EventManAppService) appService).setLoginUI(loginUI);

        DashNavigateListener dashListener = new DashNavigateListener() {
            @Override
            public void createEventTriggered() {
                ((EventManAppService) appService).getCreateEventUI().start(primaryStage);
            }

            @Override
            public void myEventsTriggered() {
                ((EventManAppService) appService).getMyEventUI().start(primaryStage);
            }

            @Override
            public void eventTimelineTriggered() {
                try {
                    ((EventManAppService) appService).getEventTimelineUI().start(primaryStage);
                    LoadEventTimeline.load(appService, ((EventManAppService) appService).getEventTimelineUI().getEventTimeline().getEvents());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void logsTriggered() {
                ((EventManAppService) appService).getLogsUI().start(primaryStage);
                ((EventManAppService) appService).getLogsUI().getLogsArea().getLogArea().setText(((EventManAppService) appService)._loadLogs());
            }

            @Override
            public void logoTriggered() {
                ((EventManAppService) appService).getDashboardUI().start(primaryStage);
            }

            @Override
            public void returnTriggered() {
                try {
                    ((EventManAppService) appService).getEventDetailsUI().start(primaryStage);
                    if (((EventManAppService) appService).getBudgetTracker().getExpenses() == null) {
                        ((EventManAppService) appService).setBudgetTracker(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void budgetUpdateTriggered() {
                if (UpdateBudgetTracker.validate(appService, budgetTrackerUI)) {
                    appService.updateEvent(EventFormEvents.UPDATE_BUDGET);
                }
            }

            @Override
            public void accountTriggered() {

            }
        };
        CreateEventListener ceventListener = new CreateEventListener() {
            @Override
            public void onConfirm() {
                String eventName = ((EventManAppService) appService).getCreateEventUI().getEventDetails().getEventNameField().getText();
                LocalDate startDate = ((EventManAppService) appService).getCreateEventUI().getEventDetails().getStartDatePicker().getValue();
                LocalDate endDate = ((EventManAppService) appService).getCreateEventUI().getEventDetails().getEndDatePicker().getValue();

                if(ValidateNewEventService.validate(eventName, startDate, endDate, appService)) {
                    appService.createEvent(new Event(eventName,startDate,endDate));
                    ValidateNewEventService.addEvent(appService.getSelectedEvent(), appService);
                    ((EventManAppService) appService).getEventDetailsUI().start(primaryStage);
                }
            }

            @Override
            public void onCancel() {
                ((EventManAppService) appService).getDashboardUI().start(primaryStage);
            }
        };
        EventDetailListener eventDetailListener = new EventDetailListener() {
            @Override
            public void onOpen() {
                PopulateDetails.populateEventDetails(appService, eventDetailsUI);
                PopulateDetails.populateBudgetTracker(appService, budgetTrackerUI);
            }

            @Override
            public void onSelectEvent() {
                appService.setSaveStatus(SaveStatus.SAVED);
                ((EventManAppService) appService).getEventDetailsUI().start(primaryStage);
            }
        };
        EventFormListener eventFormListener = new EventFormListener() {
            @Override
            public void startDateUpdated() {
                LocalDate date = eventDetailsUI.getEventForm().getStartDatePicker().getValue();
                appService.updateEvent(EventFormEvents.START_DATE, date);
            }

            @Override
            public void endDateUpdated() {
                LocalDate date = eventDetailsUI.getEventForm().getEndDatePicker().getValue();
                appService.updateEvent(EventFormEvents.END_DATE, date);
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void startTimeUpdated() {
                String hour = ((EventManAppService) appService).getEventDetailsUI().getEventForm().getTimeStartField().getHourDropdown().getValue();
                String min = ((EventManAppService) appService).getEventDetailsUI().getEventForm().getTimeStartField().getMinuteDropdown().getValue();
                appService.updateEvent(EventFormEvents.START_TIME, LocalTime.parse(hour + ":" + min, DateTimeFormatter.ofPattern("HH:mm")));
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void endTimeUpdated() {
                String hour = ((EventManAppService) appService).getEventDetailsUI().getEventForm().getTimeEndField().getHourDropdown().getValue();
                String min = ((EventManAppService) appService).getEventDetailsUI().getEventForm().getTimeEndField().getMinuteDropdown().getValue();
                appService.updateEvent(EventFormEvents.END_TIME, LocalTime.parse(hour + ":" + min, DateTimeFormatter.ofPattern("HH:mm")));
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void descriptionUpdated() {
                String description = eventDetailsUI.getEventForm().getEventDescArea().getText();
                appService.updateEvent(EventFormEvents.DESC, description);
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void guestsUpdated() {
                appService.setSaveStatus(SaveStatus.UNSAVED);
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getGuestsField().setText(RegexService.parse(eventDetailsUI.getEventForm().getGuestEmailsArea().getText())); // if email add is valid, increment guests counter
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void sendEmail(){
                try {
                    appService.inviteGuests(eventDetailsUI.getEventForm().getGuestEmailsArea().getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onUpdate() {
                appService.updateEvent(EventFormEvents.UPDATE_CHANGES, eventDetailsUI.getEventForm().getGuestEmailsArea().getText());
                ((EventManAppService) appService).getEventDetailsUI().getEventForm().getUpdateButton().setDisable(true);
            }

            @Override
            public void onReturn() {
                if(appService.getSaveStatus().equals(SaveStatus.UNSAVED)) {
                    if(MainFrameAlerts.saveChanges()) {
                        onUpdate();
                    }
                }
                try {
                    ((EventManAppService) appService).getDashboardUI().start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBudgetTracker() {
                ((EventManAppService) appService).getBudgetTrackerUI().start(primaryStage);
            }

            @Override
            public void onExport() throws IOException {
                EventReportPDFService.generateReport(appService);
            }

            @Override
            public void onInbox() {

            }

            @Override
            public void onDelete() {
                appService.removeEvent();
            }
        };

        /// modified
        dashboardUI = new DashboardUI(appService, dashListener, eventDetailListener);
        myEventUI = new MyEventUI(appService, dashListener, eventDetailListener);
        createEventUI = new CreateEventUI(appService, dashListener, ceventListener);
        eventDetailsUI = new EventDetailsUI(appService, dashListener, eventDetailListener, eventFormListener);
        eventTimelineUI = new EventTimelineUI(appService, dashListener);
        budgetTrackerUI = new BudgetTrackerUI(appService, dashListener);
        logsUI = new LogsUI(appService, dashListener);

        ((EventManAppService) appService).setDashboardUI(dashboardUI);
        ((EventManAppService) appService).setMyEventUI(myEventUI);
        ((EventManAppService) appService).setCreateEventUI(createEventUI);
        ((EventManAppService) appService).setEventDetailsUI(eventDetailsUI);
        ((EventManAppService) appService).setEventTimelineUI(eventTimelineUI);
        ((EventManAppService) appService).setBudgetTrackerUI(budgetTrackerUI);
        ((EventManAppService) appService).setLogsUI(logsUI);

///////////////////////////////////////////////////////////
        try {
            loginUI.start(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }

        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
}