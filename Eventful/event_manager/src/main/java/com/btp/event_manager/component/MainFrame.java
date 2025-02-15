package com.btp.event_manager.component;
import com.btp.appfx.service.AppService;
import com.btp.dashboard.component.CreateEventUI;
import com.btp.dashboard.component.DashboardUI;
import com.btp.dashboard.component.Sidebar;
import com.btp.dashboard.service.DashNavigateListener;
import com.btp.event_manager.service.EventManAppService;
import com.btp.event_manager.model.EventManState;
import com.btp.event_manager.service.EventManCommandService;
import com.btp.login.components.LoginUI;
import com.btp.login.service.LoginSuccessListener;
import javafx.application.Application;
import javafx.stage.Stage;

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

        loginUI = new LoginUI(appService, () -> dashboardUI.start(primaryStage));

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
        dashboardUI = new DashboardUI(appService, listener);
        createEventUI = new CreateEventUI(appService, listener);

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
