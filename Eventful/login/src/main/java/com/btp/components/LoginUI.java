package com.btp.components;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load Logo for Application Icon (Increases size)
        Image logoImg = new Image(getClass().getResourceAsStream("/logo.png"));
        ImageView logoicon = new ImageView(logoImg);
        logoicon.setFitWidth(64); // Adjust width
        logoicon.setFitHeight(64); // Adjust height
        primaryStage.getIcons().add(logoImg);


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

        HBox usernameFieldContainer = new HBox(5);
        usernameFieldContainer.setAlignment(Pos.CENTER_LEFT);
        usernameFieldContainer.setPrefWidth(300); //
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefWidth(280); // Set width
        HBox.setHgrow(usernameField, Priority.ALWAYS);
        Image profimg = new Image(getClass().getResourceAsStream("/profileIcon.png"));
        ImageView profileicon = new ImageView(profimg);
        profileicon.setFitWidth(30); // icon size
        profileicon.setPreserveRatio(true);
        usernameFieldContainer.getChildren().addAll(usernameField, profileicon);
        usernameBox.getChildren().addAll(usernameLabel, usernameFieldContainer);


        // Password Field with Label (Using VBox)
        VBox passwordBox = new VBox(5);
        passwordBox.setAlignment(Pos.BASELINE_LEFT); // Left align the password box
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle("-fx-font-size: 10px;");
        // HBox to contain password field and icon
        HBox passwordFieldContainer = new HBox(5);
        passwordFieldContainer.setAlignment(Pos.CENTER_LEFT);
        passwordFieldContainer.setPrefWidth(300);
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefWidth(280); // Set width
        HBox.setHgrow(passwordField, Priority.ALWAYS);

        Image passimg = new Image(getClass().getResourceAsStream("/passIcon.png"));
        ImageView passicon = new ImageView(passimg);
        passicon.setFitWidth(30); // Adjust size for better alignment
        passicon.setPreserveRatio(true);
        passwordFieldContainer.getChildren().addAll(passwordField, passicon);
        passwordBox.getChildren().addAll(passwordLabel, passwordFieldContainer);


        // Forgot Password Link (Right Aligned)
        HBox forgotPasswordBox = new HBox();
        forgotPasswordBox.setAlignment(Pos.BASELINE_RIGHT);
        Hyperlink forgotPassword = new Hyperlink("Forgot password?");
        forgotPassword.setStyle("-fx-font-size: 10px;");
        forgotPasswordBox.getChildren().add(forgotPassword);

        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        loginButton.setMaxWidth(300);

        // Create New User Button
        Button createUserButton = new Button("Create new user");
        createUserButton.setStyle("-fx-background-color: transparent;-fx-border-color: purple; -fx-text-fill: purple; -fx-font-size: 14px; -fx-border-radius: 5px;");
        createUserButton.setMaxWidth(300);

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
        primaryStage.setTitle("Login UI");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
