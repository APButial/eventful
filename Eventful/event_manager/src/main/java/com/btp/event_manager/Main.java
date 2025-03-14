package com.btp.event_manager;

import com.btp.event_manager.component.Splash;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage mainstage) {
        // Set Main Scene
        mainstage.setScene(new Splash(mainstage).getScene());
        mainstage.setTitle("Welcome to Eventful");
        mainstage.setResizable(false);
        mainstage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
