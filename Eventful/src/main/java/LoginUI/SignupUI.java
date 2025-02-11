package LoginUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SignupUI extends Application {
    @Override
    public void start(Stage primaryStage) {

        VBox leftPanel = new VBox(20);
        leftPanel.setStyle("-fx-background-color: white; -fx-padding: 40px; -fx-border-radius: 15px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        leftPanel.setPrefWidth(300);
        leftPanel.setAlignment(Pos.TOP_CENTER);

        // Logo bit
        HBox logo = new HBox();
        logo.setAlignment(Pos.CENTER);
        logo.setPadding(new javafx.geometry.Insets(0, 0, 10, 0)); // padding for logo
        Image logoImage = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(120);
        logoView.setPreserveRatio(true);
        logo.getChildren().add(logoView);

        // Login Text
        Label loginText = new Label("Sign up for an account");
        loginText.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        // Username Field with Label (Using VBox)
        VBox usernameBox = new VBox(5);
        usernameBox.setAlignment(Pos.BASELINE_LEFT); // Left align the username box
        Label usernameLabel = new Label("USERNAME");
        usernameLabel.setStyle("-fx-font-size: 10px;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(250);
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field with Label (Using VBox)
        VBox passwordBox = new VBox(5);
        passwordBox.setAlignment(Pos.BASELINE_LEFT); // Left align the password box
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-font-size: 10px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(250);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
        // Re enter Password Field with Label (Using VBox)

        VBox ReenterBox = new VBox(5);
        ReenterBox.setAlignment(Pos.BASELINE_LEFT); // Left align the password box
        Label reenterLabel = new Label("Re-Enter Password");
        reenterLabel.setStyle("-fx-font-size: 10px;");
        PasswordField renterField = new PasswordField();
        renterField.setPromptText("Re-Enter your password");
        renterField.setMaxWidth(300);
        passwordBox.getChildren().addAll(reenterLabel, renterField);


        // Login Button
        Button loginButton = new Button("Confirm");
        loginButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        loginButton.setMaxWidth(300);

        // Create New User Button
        Button createUserButton = new Button("Canel");
        createUserButton.setStyle("-fx-border-color: purple; -fx-text-fill: purple; -fx-font-size: 14px; -fx-border-radius: 5px;");
        createUserButton.setMaxWidth(300);

        // "OR" separator line
        HBox orline = new HBox(10);
        orline.setAlignment(Pos.CENTER);
        Label orSeparatorLabel = new Label("--------------- OR ---------------");
        orSeparatorLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;");
        orline.getChildren().add(orSeparatorLabel);

        // Add elements to left panel
        leftPanel.getChildren().addAll(logo, loginText, usernameBox, passwordBox,  loginButton, orline, createUserButton);

        // Right Panel (Background Image) - 70% Width
        Image bgImage = new Image(getClass().getResourceAsStream("/login.jpg"));
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        Pane rightPanel = new Pane();
        rightPanel.setBackground(new Background(backgroundImage));

        // Layout using BorderPane (Left & Right)
        BorderPane root = new BorderPane();
        root.setLeft(leftPanel);
        root.setCenter(rightPanel);

        // Scene & Stage
        Scene scene = new Scene(root, 1024, 600, Color.WHITE);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SignUp UI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



