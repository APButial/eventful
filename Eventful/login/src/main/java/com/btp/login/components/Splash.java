package com.btp.login.components;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Splash extends StackPane {
    private Scene scene;

    public Splash(Stage stage) {
        // Load Splash Image from Resources
        Image splashImg = new Image(getClass().getResourceAsStream("/splash.png"));
        ImageView splashImage = new ImageView(splashImg);
        splashImage.setFitWidth(900);  // Set width
        splashImage.setFitHeight(600); // Set height

        // Add the image to the layout
        this.getChildren().add(splashImage);

        // Create the scene
        scene = new Scene(this, 900, 600);

        // Set the scene to the stage
        stage.setScene(scene);
        stage.setTitle("Splash Screen");
        stage.show();

        // Switch to LoginUI when clicked
        scene.setOnMouseClicked(e -> {
            LoginUI loginUI = new LoginUI(); // Create instance of LoginUI
            try {
                loginUI.start(stage); // Start LoginUI
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
