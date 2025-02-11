package practice_ground;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class demo extends Application {

    Scene login, home, splash;

    @Override
    public void start(Stage Main) {
        // Load Logo for Application Icon (Increases size)
        Image logoImg = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImg);
        logoView.setFitWidth(64); // Adjust width
        logoView.setFitHeight(64); // Adjust height
        Main.getIcons().add(logoImg);

        // Load Splash Image (Fix path issue)
        Image splashImg = new Image(getClass().getResourceAsStream("/splash.png"));
        ImageView splashImage = new ImageView(splashImg);
        splashImage.setFitWidth(900);
        splashImage.setFitHeight(600);

        // Splash Screen Layout
        StackPane splashLayout = new StackPane(splashImage);
        splash = new Scene(splashLayout, 900, 600);

        // Click on splash to switch to login
        splash.setOnMouseClicked(e -> {
            Main.setScene(login);
            Main.setTitle("Login");
        });

        // Login Screen
        Label label1 = new Label("Welcome to Login Page");
        Button button1 = new Button("Go to Home Page");
        button1.setOnAction(e -> {
            Main.setScene(home);
            Main.setTitle("Home");
        });

        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label1, button1);
        login = new Scene(layout1, 500, 500);

        // Home Screen
        Button button2 = new Button("Go back to Login");
        button2.setOnAction(e -> {
            Main.setScene(login);
            Main.setTitle("Login");
        });

        StackPane layout2 = new StackPane(button2);
        home = new Scene(layout2, 600, 600);

        // Show Splash Screen First
        Main.setScene(splash);
        Main.setTitle("Splash Screen");
        Main.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
