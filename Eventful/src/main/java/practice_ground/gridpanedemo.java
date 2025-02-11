package practice_ground;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class gridpanedemo extends Application {

    Scene login, home, splash;

    @Override
    public void start(Stage Main) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));//padding around layout

        grid.setVgap(8);
        grid.setHgap(10);//indiv cells

        Label name = new Label("Username");
        GridPane.setConstraints(name,0,0);

        TextField input = new TextField("user");
        GridPane.setConstraints(input,0,1);

        Label pass = new Label("Password");
        GridPane.setConstraints(name,1,1);

        TextField input2 = new TextField("password");
        GridPane.setConstraints(input,3,3);
        input2.setPromptText("Password12345");


        Scene scene=new Scene(grid, 500,500);
        grid.getChildren().addAll(name,input,input2);
        Main.setScene(scene);
        Main.show();
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
