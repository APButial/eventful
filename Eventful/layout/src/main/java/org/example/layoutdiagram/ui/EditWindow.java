package org.example.layoutdiagram.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.layoutdiagram.model.*;

import java.util.List;

public class EditWindow {
    private Stage stage;
    private Furniture furniture;
    private final ComboBox<String> guestComboBox;
    private final ListView<String> assignedGuestsListView;
    private final GuestList guestList;

    public EditWindow(Furniture furniture) {
        this.furniture = furniture;
        this.stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL); // Make window modal
        stage.setAlwaysOnTop(true); // Make window always on top
        stage.initStyle(StageStyle.UTILITY); // Remove minimize/maximize buttons
        stage.setResizable(false); // Prevent resizing
        stage.setTitle("Edit Furniture");
        this.guestComboBox = new ComboBox<>();
        this.assignedGuestsListView = new ListView<>();
        this.guestList = GuestList.getInstance();
        initWindow();
    }

    private void initWindow() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 10;");
        content.setAlignment(Pos.TOP_LEFT);
        
        if (furniture instanceof Table) {
            // Multiple guest assignment for tables
            Label guestLabel = new Label("Add Guest:");
            
            updateGuestComboBox();
            
            Button addButton = new Button("Add");
            addButton.setOnAction(e -> {
                String selectedGuest = guestComboBox.getValue();
                if (selectedGuest != null && !selectedGuest.equals("None")) {
                    Table table = (Table) furniture;
                    table.addAssignedGuest(selectedGuest);
                    guestList.assignGuestToTable(selectedGuest, table);
                    updateGuestComboBox();
                    updateAssignedGuestsList();
                }
            });
            
            HBox addGuestBox = new HBox(10);
            addGuestBox.setAlignment(Pos.CENTER_LEFT);
            addGuestBox.getChildren().addAll(guestLabel, guestComboBox, addButton);
            
            Label assignedLabel = new Label("Assigned Guests:");
            assignedGuestsListView.setPrefHeight(150);
            
            Button removeButton = new Button("Remove Selected");
            removeButton.setOnAction(e -> {
                String selectedGuest = assignedGuestsListView.getSelectionModel().getSelectedItem();
                if (selectedGuest != null) {
                    Table table = (Table) furniture;
                    table.removeAssignedGuest(selectedGuest);
                    guestList.unassignFromTable(selectedGuest);
                    updateGuestComboBox();
                    updateAssignedGuestsList();
                }
            });
            
            content.getChildren().addAll(
                addGuestBox,
                assignedLabel,
                assignedGuestsListView,
                removeButton
            );
            
            updateAssignedGuestsList();
            
        } else if (furniture instanceof Chair) {
            // Single guest assignment for chairs
            Label guestLabel = new Label("Assigned Guest:");
            
            updateGuestComboBox();
            
            Button assignButton = new Button("Assign");
            assignButton.setOnAction(e -> {
                String previousGuest = furniture.getAssignedGuest();
                String selectedGuest = guestComboBox.getValue();
                
                if (previousGuest != null) {
                    guestList.unassignFromChair(previousGuest);
                }
                
                if (selectedGuest != null && !selectedGuest.equals("None")) {
                    furniture.setAssignedGuest(selectedGuest);
                    guestList.assignGuestToChair(selectedGuest, furniture);
                } else {
                    furniture.setAssignedGuest(null);
                }
                
                updateGuestComboBox();
            });
            
            HBox guestBox = new HBox(10);
            guestBox.setAlignment(Pos.CENTER_LEFT);
            guestBox.getChildren().addAll(guestLabel, guestComboBox, assignButton);
            
            content.getChildren().add(guestBox);
        }

        Scene scene = new Scene(content, 300, furniture instanceof Table ? 300 : 100);
        stage.setScene(scene);
    }

    private void updateGuestComboBox() {
        String currentGuest = furniture.getAssignedGuest();
        
        guestComboBox.getItems().clear();
        guestComboBox.getItems().add("None");
        
        List<String> availableGuests;
        if (furniture instanceof Table) {
            availableGuests = guestList.getUnassignedGuestsForTable();
        } else {
            availableGuests = guestList.getUnassignedGuestsForChair();
            if (currentGuest != null) {
                availableGuests.add(currentGuest);
            }
        }
        
        guestComboBox.getItems().addAll(availableGuests);
        if (furniture instanceof Chair) {
            guestComboBox.setValue(currentGuest != null ? currentGuest : "None");
        } else {
            guestComboBox.setValue("None");
        }
    }

    private void updateAssignedGuestsList() {
        if (furniture instanceof Table) {
            assignedGuestsListView.getItems().clear();
            assignedGuestsListView.getItems().addAll(furniture.getAssignedGuests());
        }
    }

    public void show() {
        stage.showAndWait(); // Use showAndWait to block until window is closed
    }
} 