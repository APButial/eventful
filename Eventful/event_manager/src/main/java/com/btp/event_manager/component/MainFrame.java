package com.btp.event_manager.component;
import com.btp.appfx.enums.SaveStatus;
import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.CreateEventUI;
import com.btp.dashboard.component.DashboardUI;
import com.btp.dashboard.component.EventDetailsUI;
import com.btp.dashboard.service.CreateEventListener;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.dashboard.service.EventDetailListener;
import com.btp.dashboard.service.EventFormListener;
import com.btp.event_manager.model.Event;
import com.btp.event_manager.service.*;
import com.btp.event_manager.model.EventManState;
import com.btp.login.components.LoginUI;
import com.btp.login.service.LoginSuccessListener;
import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainFrame extends Application {
    private AppService appService;
    private EventManState eventManState;
    private EventManAppService eventManAppService;
    private LoginUI loginUI;
    private DashboardUI dashboardUI;
    private CreateEventUI createEventUI;
    private EventDetailsUI eventDetailsUI;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        eventManState = new EventManState();
        eventManAppService = new EventManAppService(eventManState);
        appService = new EventManCommandService(eventManAppService);

        loginUI = new LoginUI(appService, new LoginSuccessListener() {
            @Override
            public void onLoginSuccess() {
                LoadUserEvents.load(appService);
                dashboardUI.start(primaryStage);
            }
        });

        DashNavigateListener dashListener = new DashNavigateListener() {
            @Override
            public void createEventTriggered() {
                    createEventUI.start(primaryStage);
            }

            @Override
            public void myEventsTriggered() {

            }

            @Override
            public void eventTimelineTriggered() {

            }

            @Override
            public void logsTriggered() {

            }
        };
        CreateEventListener ceventListener = new CreateEventListener() {
            @Override
            public void onConfirm() {
                String eventName = createEventUI.getEventDetails().getEventNameField().getText();
                LocalDate startDate = createEventUI.getEventDetails().getStartDatePicker().getValue();
                LocalDate endDate = createEventUI.getEventDetails().getEndDatePicker().getValue();

                if(ValidateNewEventService.validate(eventName, startDate, endDate, appService)) {
                    Event event = new Event(eventName, startDate, endDate);
                    event.setLastAccessed(appService.getSysDateTime());
                    appService.getCurrUser().getEvents().add(event);
                    ValidateNewEventService.addEvent(event, appService);

                    appService.setSelectedEvent(event);
                    eventDetailsUI.start(primaryStage);
                }
            }
        };
        EventDetailListener eventDetailListener = new EventDetailListener() {
            @Override
            public void onOpen() {
                PopulateEventDetails.populate(appService, eventDetailsUI);
            }
        };
        EventFormListener eventFormListener = new EventFormListener() {
            @Override
            public void startDateUpdated() {
                appService.setStartDate(eventDetailsUI.getEventForm().getStartDatePicker().getValue());
                appService.setSaveStatus(SaveStatus.UNSAVED);
            }

            @Override
            public void endDateUpdated() {
                appService.setEndDate(eventDetailsUI.getEventForm().getEndDatePicker().getValue());
                appService.setSaveStatus(SaveStatus.UNSAVED);
            }

            @Override
            public void startTimeUpdated() {
            }

            @Override
            public void endTimeUpdated() {
            }

            @Override
            public void descriptionUpdated() {
                appService.setDescription(eventDetailsUI.getEventForm().getEventDescArea().getText().strip());
                appService.setSaveStatus(SaveStatus.UNSAVED);
            }

            @Override
            public void guestsUpdated() {

            }

            @Override
            public void onUpdate() {
                WriteEventsService.overwrite(appService);
                appService.setSaveStatus(SaveStatus.SAVED);
            }

            @Override
            public void onReturn() {
                if(appService.getSaveStatus().equals(SaveStatus.UNSAVED)) {

                }
            }
        };

        dashboardUI = new DashboardUI(appService, dashListener);
        createEventUI = new CreateEventUI(appService, dashListener, ceventListener);
        eventDetailsUI = new EventDetailsUI(appService, dashListener, eventDetailListener, eventFormListener);

///////////////////////////////////////////////////////////
        try {
            appService.setPrevApplication(this);
            loginUI.start(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
