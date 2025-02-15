package com.btp.login.components;

import com.btp.appfx.service.AppService;
import com.btp.login.service.LoginSuccessListener;
import com.btp.login.service.VerifyLoginService;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Data;

@Data
public class LoginUI extends Application {
    private AppService appService;
    private TextField usernameField;
    private TextField passwordField;
    private SignupUI signupUI;
    private LoginSuccessListener listener;


    public LoginUI(AppService appService, LoginSuccessListener loginSuccessListener) {
        this.appService = appService;
        this.listener = loginSuccessListener;
    }
    
    @Override
    public void start(Stage primaryStage) {
        signupUI = new SignupUI(appService);



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
        Label loginText = new Label("Login into your account");
        loginText.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        // Username Field with Label (Using VBox)
        VBox usernameBox = new VBox(5);
        usernameBox.setAlignment(Pos.BASELINE_LEFT); // Left align the username box
        Label usernameLabel = new Label("USERNAME");
        usernameLabel.setStyle("-fx-font-size: 10px;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setMaxWidth(250);
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password Field with Label (Using VBox)
        VBox passwordBox = new VBox(5);
        passwordBox.setAlignment(Pos.BASELINE_LEFT); // Left align the password box
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-font-size: 10px;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(250);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Forgot Password Link (Right Aligned)
        HBox forgotPasswordBox = new HBox();
        forgotPasswordBox.setAlignment(Pos.BASELINE_RIGHT);
        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setStyle("-fx-font-size: 10px;");
        forgotPasswordBox.getChildren().add(forgotPassword);

        // Login Button
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login");
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        loginButton.setMaxWidth(300);
        loginButton.setOnAction(event -> {
            if(VerifyLoginService.verify(usernameField.getText(),passwordField.getText())) {
                appService.setPrevApplication(this);
                appService.setCreator(usernameField.getText());
                listener.onLoginSuccess();
            } else {
                alert.setHeaderText("Login Unsuccessful");
                alert.setContentText("You entered an invalid username/password. Please try again");
                alert.showAndWait();
            }
        });

        // Create New BaseUser Button
        Button createUserButton = new Button("Create new user");
        createUserButton.setStyle("-fx-border-color: purple; -fx-text-fill: purple; -fx-font-size: 14px; -fx-border-radius: 5px;");
        createUserButton.setMaxWidth(300);
        createUserButton.setOnAction(event -> {
            try {
                appService.setPrevApplication(this);
                signupUI.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // "OR" separator line
        HBox orline = new HBox(10);
        orline.setAlignment(Pos.CENTER);
        Label orSeparatorLabel = new Label("--------------- OR ---------------");
        orSeparatorLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        orline.getChildren().add(orSeparatorLabel);

        // Add elements to left panel
        leftPanel.getChildren().addAll(logo, loginText, usernameBox, passwordBox, forgotPasswordBox, loginButton, orline, createUserButton);

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
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
