package com.btp.layout.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.btp.layout.model.GuestList;

public class GuestListWindow extends Stage {
    private final ListView<String> guestListView;
    private final TextField nameField;
    private final GuestList guestList;

    public GuestListWindow() {
        this.guestList = GuestList.getInstance();
        
        setTitle("Guest List");
        
        // Create UI components
        Label nameLabel = new Label("Guest Name:");
        nameField = new TextField();
        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        guestListView = new ListView<>();
        
        // Layout for input section
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER_LEFT);
        inputBox.getChildren().addAll(nameLabel, nameField, addButton);
        
        // Main layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(inputBox, guestListView, deleteButton);
        
        // Set up event handlers
        addButton.setOnAction(e -> addGuest());
        deleteButton.setOnAction(e -> deleteSelectedGuest());
        
        // Update list view with current guests
        refreshGuestList();
        
        // Set up the scene
        Scene scene = new Scene(layout, 300, 400);
        setScene(scene);
    }
    
    private void addGuest() {
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            guestList.addGuest(name);
            nameField.clear();
            refreshGuestList();
        }
    }
    
    private void deleteSelectedGuest() {
        String selectedGuest = guestListView.getSelectionModel().getSelectedItem();
        if (selectedGuest != null) {
            guestList.removeGuest(selectedGuest);
            refreshGuestList();
        }
    }
    
    private void refreshGuestList() {
        guestListView.getItems().clear();
        guestListView.getItems().addAll(guestList.getGuests());
    }
} 