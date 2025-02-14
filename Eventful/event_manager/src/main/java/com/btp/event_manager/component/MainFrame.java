package com.btp.event_manager.component;
import com.btp.appfx.service.AppService;
import com.btp.event_manager.model.EventManAppService;
import com.btp.event_manager.model.EventManState;
import com.btp.login.components.LoginUI;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFrame extends Application {
    private AppService appService;
    private EventManState eventManState;
    private EventManAppService eventManAppService;
    private LoginUI loginUI;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        eventManState = new EventManState();
        eventManAppService = new EventManAppService(eventManState);
        loginUI = new LoginUI(appService);

        try {
            loginUI.start(primaryStage);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
