package com.btp.event_manager.component;
import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.CreateEventUI;
import com.btp.dashboard.component.DashboardUI;
import com.btp.dashboard.component.Sidebar;
import com.btp.dashboard.service.CreateEventListener;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.event_manager.model.Event;
import com.btp.event_manager.service.EventManAppService;
import com.btp.event_manager.model.EventManState;
import com.btp.event_manager.service.EventManCommandService;
import com.btp.event_manager.service.LoadUserEvents;
import com.btp.event_manager.service.ValidateNewEventService;
import com.btp.login.components.LoginUI;
import com.btp.login.service.LoginSuccessListener;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MainFrame extends Application {
    private AppService appService;
    private EventManState eventManState;
    private EventManAppService eventManAppService;
    private LoginUI loginUI;
    private DashboardUI dashboardUI;
    private CreateEventUI createEventUI;

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

        DashNavigateListener listener = new DashNavigateListener() {
            @Override
            public void createEventTriggered() {
                try {
                    createEventUI.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        CreateEventListener listener1 = new CreateEventListener() {
            @Override
            public void onConfirm() {
                String eventName = createEventUI.getEventDetails().getEventNameField().getText();
                LocalDate startDate = createEventUI.getEventDetails().getStartDatePicker().getValue();
                LocalDate endDate = createEventUI.getEventDetails().getEndDatePicker().getValue();

                if(ValidateNewEventService.validate(eventName, startDate, endDate)) {
                    Event event = new Event(eventName, startDate, endDate);
                    appService.getCurrUser().getEvents().add(event);
                    ValidateNewEventService.addEvent(event, appService);
                }
            }
        };


        dashboardUI = new DashboardUI(appService, listener);
        createEventUI = new CreateEventUI(appService, listener, listener1);

///////////////////////////////////////////////////////////
        eventManAppService.setMainFrame(this);
        try {
            appService.setPrevApplication(this);
            loginUI.start(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}
