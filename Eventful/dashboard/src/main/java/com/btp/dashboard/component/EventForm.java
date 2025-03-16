package com.btp.dashboard.component;

    import com.btp.appfx.enums.EventFormEvents;
    import com.btp.appfx.service.AppService;
    import com.btp.dashboard.service.EventFormListener;
    import javafx.beans.value.ChangeListener;
    import javafx.beans.value.ObservableValue;
    import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

    import javax.mail.internet.AddressException;
    import java.time.LocalDate;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;
    import java.util.ArrayList;
    import java.util.List;

public class EventForm {
    private CustomDatePicker startDatePicker;
    private CustomDatePicker endDatePicker;
    private TextArea eventDescArea;
    private TextField guestsField;
    private TextArea guestEmailsArea;
    private Button sendEmailButton;
    private EventFormListener eventFormListener;
    private Button returnButton;
    private Button updateButton;
    private MilitaryTimePicker timeStartField;
    private MilitaryTimePicker timeEndField;
    private AppService appService;

    public EventForm (AppService appService, EventFormListener eventFormListener) {
        this.eventFormListener = eventFormListener;
        this.appService = appService;
    }

    public Node getComponent() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        // Start Date
        Label startDateLabel = new Label("Start Date*");
        startDatePicker = new CustomDatePicker(appService, false);
        startDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                eventFormListener.startDateUpdated();
            }
        });
        startDatePicker.setPrefWidth(240);


        HBox startDateBox = new HBox(5, startDatePicker);

        // End Date
        Label endDateLabel = new Label("End Date*");
        endDatePicker = new CustomDatePicker(appService, true);
        endDatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate localDate, LocalDate t1) {
                eventFormListener.endDateUpdated();
            }
        });
        endDatePicker.setPrefWidth(240);

        HBox endDateBox = new HBox(5, endDatePicker);

        // Time Start
        Label timeStartLabel = new Label("Time Start");
        timeStartField = new MilitaryTimePicker();
        timeStartField.getHourDropdown().setOnAction(event -> {
            eventFormListener.startTimeUpdated();
        });
        timeStartField.getMinuteDropdown().setOnAction(event -> {
            eventFormListener.startTimeUpdated();
        });
        timeStartField.setPrefWidth(210);
        timeStartField.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: transparent; -fx-padding: 5px; -fx-border-radius: 5px;");
        ImageView timeStartIcon = new ImageView(new Image("/time.png"));
        timeStartIcon.setFitWidth(30);
        timeStartIcon.setPreserveRatio(true);
        HBox timeStartBox = new HBox(5, timeStartField, timeStartIcon);

        // Time End
        Label timeEndLabel = new Label("Time End");
        timeEndField = new MilitaryTimePicker();
        timeEndField.getHourDropdown().setOnAction(event -> {
            eventFormListener.endTimeUpdated();
        });
        timeEndField.getMinuteDropdown().setOnAction(event ->  {
            eventFormListener.endTimeUpdated();
        });
        timeEndField.setPrefWidth(210);
        timeEndField.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: transparent; -fx-padding: 5px; -fx-border-radius: 5px;");
        ImageView timeEndIcon = new ImageView(new Image("/time.png"));
        timeEndIcon.setFitWidth(30);
        timeEndIcon.setPreserveRatio(true);
        HBox timeEndBox = new HBox(5, timeEndField, timeEndIcon);

        // Event Description
        Label eventDescLabel = new Label("Event Description");
        eventDescArea = new TextArea();
        eventDescArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                eventFormListener.descriptionUpdated();
            }
        });
        eventDescArea.setPrefWidth(400);
        eventDescArea.setPrefHeight(100);
        eventDescArea.setPromptText("Enter Event Description (Optional)");
        eventDescArea.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");

        // Left Section
        VBox leftSection = new VBox(10, startDateLabel, startDateBox, endDateLabel, endDateBox, timeStartLabel, timeStartBox, timeEndLabel, timeEndBox, eventDescLabel, eventDescArea);

        // Right Section
        Label guestsLabel = new Label("No. of Guests");
        guestsField = new TextField();
        guestsField.setEditable(false);
        guestsField.setPromptText("0");
        guestsField.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");
        Button viewSeatsButton = new Button("View seat assignment");

        viewSeatsButton.setStyle("-fx-font-size: 10px; -fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: blue; -fx-border-radius: 5px; ");
        HBox viewSeatsBox = new HBox(viewSeatsButton);
        viewSeatsBox.setAlignment(Pos.TOP_RIGHT);

        Label guestEmailsLabel = new Label("Guests");
        guestEmailsArea = new TextArea();
        guestEmailsArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                eventFormListener.guestsUpdated();
            }
        });
        guestEmailsArea.setPromptText("Enter guests’ email addresses (Optional)");
        guestEmailsArea.setPrefHeight(165);
        guestEmailsArea.setStyle(
                "-fx-background-color: #F5F5F5; " +
                        "-fx-border-color: transparent; " +
                        "-fx-padding: 5px; " +
                        "-fx-border-radius: 5px; " +
                        "-fx-control-inner-background: #F5F5F5;");

        sendEmailButton = new Button("Send Email Invitation");
        sendEmailButton.setOnAction(event -> {
            try {
                eventFormListener.sendEmail();
            } catch (AddressException e) {
                throw new RuntimeException(e);
            }
        });
        sendEmailButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        sendEmailButton.setPrefWidth(200);

        Button configureLayoutButton = new Button("Configure Layout");
        configureLayoutButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        configureLayoutButton.setPrefWidth(200);

        Button expensesTrackerButton = new Button("Expenses Tracker");
        expensesTrackerButton.setStyle("-fx-background-color: transparent; -fx-border-color: purple; -fx-text-fill: purple; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        expensesTrackerButton.setPrefWidth(200);
        expensesTrackerButton.setOnAction(event -> {
            eventFormListener.onBudgetTracker();
        });

        returnButton = new Button("Return");
        returnButton.setOnAction(event -> {
            eventFormListener.onReturn();
        });
        returnButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        returnButton.setPrefWidth(200);

        updateButton = new Button("Update");
        updateButton.setOnAction(event -> {
            eventFormListener.onUpdate();
        });
        updateButton.setStyle("-fx-background-color: purple; -fx-text-fill: white; -fx-border-radius: 5px; -fx-padding: 10px 20px;");
        updateButton.setPrefWidth(200);

        VBox confirmVBox = new VBox(10, returnButton, updateButton);
        confirmVBox.setAlignment(Pos.TOP_RIGHT);
        confirmVBox.setPrefHeight(100);

        VBox buttonBox = new VBox(10, sendEmailButton, configureLayoutButton, expensesTrackerButton);
        buttonBox.setPrefHeight(100);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        HBox grouper = new HBox(20, buttonBox, confirmVBox);


        VBox rightSection = new VBox(10, guestsLabel, guestsField, viewSeatsButton,viewSeatsBox, guestEmailsLabel, guestEmailsArea, grouper);

        // Main Container
        HBox formContainer = new HBox(20, leftSection, rightSection);
        formContainer.setPadding(new Insets(20, 30, 40, 30));
        formContainer.setStyle("-fx-background-color: #FFFFFF; -fx-border-radius: 10px; -fx-padding: 20px; -fx-border-color: #CCCCCC; -fx-border-width: 1px;");

        return formContainer; // Return as Node
    }

    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    public void setStartDatePicker(CustomDatePicker startDatePicker) {
        this.startDatePicker = startDatePicker;
    }

    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    public TextArea getEventDescArea() {
        return eventDescArea;
    }

    public void setEventDescArea(TextArea eventDescArea) {
        this.eventDescArea = eventDescArea;
    }

    public TextField getGuestsField() {
        return guestsField;
    }

    public void setGuestsField(TextField guestsField) {
        this.guestsField = guestsField;
    }

    public TextArea getGuestEmailsArea() {
        return guestEmailsArea;
    }

    public void setGuestEmailsArea(TextArea guestEmailsArea) {
        this.guestEmailsArea = guestEmailsArea;
    }

    public Button getSendEmailButton() {
        return sendEmailButton;
    }

    public void setSendEmailButton(Button sendEmailButton) {
        this.sendEmailButton = sendEmailButton;
    }

    public EventFormListener getEventFormListener() {
        return eventFormListener;
    }

    public void setEventFormListener(EventFormListener eventFormListener) {
        this.eventFormListener = eventFormListener;
    }

    public Button getReturnButton() {
        return returnButton;
    }

    public void setReturnButton(Button returnButton) {
        this.returnButton = returnButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public void setUpdateButton(Button updateButton) {
        this.updateButton = updateButton;
    }

    public void setEndDatePicker(CustomDatePicker endDatePicker) {
        this.endDatePicker = endDatePicker;
    }

    public MilitaryTimePicker getTimeStartField() {
        return timeStartField;
    }

    public void setTimeStartField(MilitaryTimePicker timeStartField) {
        this.timeStartField = timeStartField;
    }

    public MilitaryTimePicker getTimeEndField() {
        return timeEndField;
    }

    public void setTimeEndField(MilitaryTimePicker timeEndField) {
        this.timeEndField = timeEndField;
    }
}
