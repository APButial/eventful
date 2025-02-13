package com.btp;

import com.btp.components.LoginUI;
import com.btp.components.Splash;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage mainstage) {
        // Set Main Scene
        mainstage.setScene(new Splash(mainstage).getScene());
        mainstage.setTitle("Main Screen");
        mainstage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
