package LoginUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    Scene  splash;

    @Override
    public void start(Stage mainstage) {
        // Load Splash Image from Resources (Fix path)
        Image splashImg = new Image(getClass().getResourceAsStream("/splash.png"));
//        if (splashImg.isError()) { // kung ayaw mag load splash
//            System.out.println("Failed to load splash image.");
//        }

        ImageView splashImage = new ImageView(splashImg);
        splashImage.setFitWidth(900);  // Set width
        splashImage.setFitHeight(600); // Set height

        // Layout for Splash Screen
        StackPane splashLayout = new StackPane(splashImage);
        splash = new Scene(splashLayout, 900, 600);

        // Set Splash Scene
        mainstage.setScene(splash);
        mainstage.setTitle("Splash Screen");
        mainstage.show();

        // Switch to LoginUI when clicked
        splash.setOnMouseClicked(e -> {
            LoginUI loginUI = new LoginUI(); // Create instance of LoginUI
            try {
                loginUI.start(mainstage); // Start LoginUI
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
