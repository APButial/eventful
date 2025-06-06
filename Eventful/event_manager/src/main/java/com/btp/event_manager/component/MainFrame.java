package com.btp.event_manager.component;
import com.btp.appfx.enums.EventFormEvents;
import com.btp.appfx.enums.SaveStatus;
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
    public void start(Stage primaryStage) {
        eventManState = new EventManState();
        appService = new EventManAppService(eventManState);
        ((EventManAppService) appService).setMainStage(primaryStage);

        Image logoImg = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImg);
        primaryStage.getIcons().add(logoImg);


        loginUI = new LoginUI(appService, new LoginSuccessListener() {
            @Override
            public void onLoginSuccess() {
                dashboardUI.start(primaryStage);
                ((EventManAppService) appService).setPrevApplication(dashboardUI);
            }

            @Override
            public void returnLogin() throws Exception {
                ((EventManAppService) appService).getPrevApplication().start(primaryStage);
            }
        });
        ((EventManAppService) appService).setLoginUI(loginUI);
        ((EventManAppService) appService).setPrevApplication(loginUI);

        DashNavigateListener dashListener = new DashNavigateListener() {
            @Override
            public void createEventTriggered() {
                    createEventUI.start(primaryStage);
            }

            @Override
            public void myEventsTriggered() {
                myEventUI.start(primaryStage);
            }

            @Override
            public void eventTimelineTriggered() {
                try {
                    eventTimelineUI.start(primaryStage);
                    LoadEventTimeline.load(appService, eventTimelineUI.getEventTimeline().getEvents());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void logsTriggered() {
                logsUI.start(primaryStage);
                logsUI.getLogsArea().getLogArea().setText(((EventManAppService) appService)._loadLogs());
            }

            @Override
            public void logoTriggered() {
                dashboardUI.start(primaryStage);
                ((EventManAppService) appService).setPrevApplication(dashboardUI);
            }

            @Override
            public void returnTriggered() {
                try {
                    ((EventManAppService) appService).getPrevApplication().start(primaryStage);
                    if (((EventManAppService) appService).getBudgetTracker().getExpenses() == null) {
                        ((EventManAppService) appService).setBudgetTracker(null);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
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
                String eventName = createEventUI.getEventDetails().getEventNameField().getText();
                LocalDate startDate = createEventUI.getEventDetails().getStartDatePicker().getValue();
                LocalDate endDate = createEventUI.getEventDetails().getEndDatePicker().getValue();

                if(ValidateNewEventService.validate(eventName, startDate, endDate, appService)) {
                    appService.createEvent(new Event(eventName,startDate,endDate));
                    ValidateNewEventService.addEvent(appService.getSelectedEvent(), appService);
                    eventDetailsUI.start(primaryStage);
                }
            }

            @Override
            public void onCancel() {
                ((EventManAppService) appService).getPrevApplication();
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
                appService.setSaveStatus(SaveStatus.SAVED);;
                eventDetailsUI.start(primaryStage);
            }
        };
        EventFormListener eventFormListener = new EventFormListener() {
            @Override
            public void startDateUpdated() {
                appService.updateEvent(EventFormEvents.START_DATE, eventDetailsUI.getEventForm().getStartDatePicker().getValue());
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void endDateUpdated() {
                appService.updateEvent(EventFormEvents.END_DATE, eventDetailsUI.getEventForm().getEndDatePicker().getValue());
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void startTimeUpdated() {
                String hour = eventDetailsUI.getEventForm().getTimeStartField().getHourDropdown().getValue();
                String min = eventDetailsUI.getEventForm().getTimeStartField().getMinuteDropdown().getValue();
                appService.updateEvent(EventFormEvents.START_TIME, LocalTime.parse(hour + ":" + min, DateTimeFormatter.ofPattern("HH:mm")));
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void endTimeUpdated() {
                String hour = eventDetailsUI.getEventForm().getTimeEndField().getHourDropdown().getValue();
                String min = eventDetailsUI.getEventForm().getTimeEndField().getMinuteDropdown().getValue();
                appService.updateEvent(EventFormEvents.END_TIME, LocalTime.parse(hour + ":" + min, DateTimeFormatter.ofPattern("HH:mm")));
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void descriptionUpdated() {
                appService.updateEvent(EventFormEvents.DESC, eventDetailsUI.getEventForm().getEventDescArea().getText());
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void guestsUpdated() {
                appService.setSaveStatus(SaveStatus.UNSAVED);
                eventDetailsUI.getEventForm().getGuestsField().setText(RegexService.parse(eventDetailsUI.getEventForm().getGuestEmailsArea().getText())); // if email add is valid, increment guests counter
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(false);
            }

            @Override
            public void sendEmail(){
                try {
                    appService.inviteGuests(eventDetailsUI.getEventForm().getGuestEmailsArea().getText());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onUpdate() {
                appService.updateEvent(EventFormEvents.UPDATE_CHANGES, eventDetailsUI.getEventForm().getGuestEmailsArea().getText());
                eventDetailsUI.getEventForm().getUpdateButton().setDisable(true);
            }

            @Override
            public void onReturn() {
                if(appService.getSaveStatus().equals(SaveStatus.UNSAVED)) {
                    if(MainFrameAlerts.saveChanges()) {
                        onUpdate();
                    }
                }
                try {
                    ((EventManAppService) appService).setPrevApplication(dashboardUI);
                    dashboardUI.start(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onBudgetTracker() {
                ((EventManAppService) appService).setPrevApplication(eventDetailsUI);
                budgetTrackerUI.start(primaryStage);
            }

            @Override
            public void onExport() throws IOException {
                EventReportPDFService.generateReport(appService);
            }

            @Override
            public void onInbox() {

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

///////////////////////////////////////////////////////////
        try {
            ((EventManAppService) appService).setPrevApplication(loginUI);
            loginUI.start(primaryStage);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
}