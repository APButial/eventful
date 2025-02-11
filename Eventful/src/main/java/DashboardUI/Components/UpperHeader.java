package DashboardUI.Components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class UpperHeader {
    private HBox upperHeader;

    public UpperHeader() {
        upperHeader = new HBox(10);
        upperHeader.setPadding(new Insets(5, 20, 5, 20));
        upperHeader.setAlignment(Pos.TOP_RIGHT);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search");
        searchBar.setPrefWidth(250);

        Button calendarButton = new Button("\uD83D\uDCC5");//idk what happend this is just the emoji for icons still works so..
        Button notificationsButton = new Button("\uD83D\uDD14");
        Button profileButton = new Button("\uD83D\uDC64");
        Button menuButton = new Button("\u22EE");

        String textButtonStyle = "-fx-background-color: transparent; -fx-border: none; -fx-font-size: 14px; -fx-text-fill: #AAB2C8;";
        calendarButton.setStyle(textButtonStyle);
        notificationsButton.setStyle(textButtonStyle);
        profileButton.setStyle(textButtonStyle);
        menuButton.setStyle(textButtonStyle);

        HBox profileMenu = new HBox(profileButton, menuButton);
        profileMenu.setSpacing(0);

        upperHeader.getChildren().addAll(searchBar, calendarButton, notificationsButton, profileMenu);
    }

    public HBox getComponent() {
        return upperHeader;
    }
}
