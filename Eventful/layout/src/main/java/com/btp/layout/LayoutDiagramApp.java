package com.btp.layout;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.core.math.FXGLMath;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Tooltip;
import javafx.stage.Popup;
import javafx.util.Duration;
import com.btp.layout.model.Furniture;
import com.btp.layout.model.FurnitureFactory;
import com.btp.layout.model.FurnitureFactory.FurnitureType;
import com.btp.layout.model.GuestList;
import com.btp.layout.ui.EditWindow;
import com.btp.layout.ui.GuestListWindow;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LayoutDiagramApp extends GameApplication {

    private static final int CELL_SIZE = 120;
    private static final int GRID_WIDTH = 50; // Changed back to 50 for larger world
    private static final int GRID_HEIGHT = 50; // Changed back to 50 for larger world
    private static final int SIDEBAR_WIDTH = 250;
    
    // World size (total size of the pannable area)
    private static final int WORLD_WIDTH = GRID_WIDTH * CELL_SIZE;
    private static final int WORLD_HEIGHT = GRID_HEIGHT * CELL_SIZE;
    
    // Window size (visible area)
    private static final int WINDOW_WIDTH = 1920; // Fixed window width
    private static final int WINDOW_HEIGHT = 1080; // Fixed window height

    private Color currentColor = Color.GREEN; // Default color for chair
    
    // Track last placement position
    private int lastPlacementX = -1;
    private int lastPlacementY = -1;
    
    // Highlight entity
    private Entity highlightEntity;
    
    // Tooltip components
    private Popup tooltipPopup;
    private Label tooltipLabel;
    private PauseTransition tooltipDelay;
    private Entity lastHoveredFurniture;

    private double lastMouseSceneX;
    private double lastMouseSceneY;

    private List<StackPane> toolButtons = new ArrayList<>();
    private StackPane selectedButton = null;

    // Store sidebar reference
    private VBox sidebar;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(WINDOW_WIDTH);
        settings.setHeight(WINDOW_HEIGHT);
        settings.setTitle("Layout Diagram");
        settings.setVersion("1.0");

    }

    @Override
    protected void initGame() {
        FXGL.getGameScene().setBackgroundColor(Color.LIGHTGRAY);
        initGrid();
        initSidebar();
        initMouseInput();
        initHighlight();
        initTooltip();


    }

    @Override
    protected void onUpdate(double tpf) {
        var viewport = FXGL.getGameScene().getViewport();
        Point2D mouse = FXGL.getInput().getMousePositionUI();

        // Only pan if mouse is within visible bounds
        if (mouse.getX() >= 0 && mouse.getX() <= FXGL.getAppWidth() &&
            mouse.getY() >= 0 && mouse.getY() <= FXGL.getAppHeight()) {
            
            double panSpeed = 500 * tpf;
            double margin = 30;

            double maxX = WORLD_WIDTH - FXGL.getAppWidth();
            double maxY = WORLD_HEIGHT - FXGL.getAppHeight();

            // Allow panning if mouse is not in the middle of the sidebar
            boolean inSidebarMiddle = mouse.getX() >= FXGL.getAppWidth() - SIDEBAR_WIDTH && 
                                    mouse.getX() < FXGL.getAppWidth() - margin;

            if (!inSidebarMiddle) {
                // Pan right — including when at sidebar edge
                if (mouse.getX() >= FXGL.getAppWidth() - margin) {
                    double newX = viewport.getX() + panSpeed;
                    viewport.setX(FXGLMath.clamp((float)newX, 0f, (float)maxX));
                }

                // Pan left
                if (mouse.getX() <= margin) {
                    double newX = viewport.getX() - panSpeed;
                    viewport.setX(FXGLMath.clamp((float)newX, 0f, (float)maxX));
                }

                // Pan down
                if (mouse.getY() >= FXGL.getAppHeight() - margin) {
                    double newY = viewport.getY() + panSpeed;
                    viewport.setY(FXGLMath.clamp((float)newY, 0f, (float)maxY));
                }

                // Pan up
                if (mouse.getY() <= margin) {
                    double newY = viewport.getY() - panSpeed;
                    viewport.setY(FXGLMath.clamp((float)newY, 0f, (float)maxY));
                }
            }
        }

        // Update sidebar position
        if (sidebar != null) {
            sidebar.setTranslateX(FXGL.getAppWidth() - SIDEBAR_WIDTH);
            sidebar.setTranslateY(0);
        }
    }

    private void initGrid() {
        // Create a container entity for the grid
        var gridEntity = FXGL.entityBuilder()
                .buildAndAttach();

        // Draw grid lines
        for (int x = 0; x <= GRID_WIDTH; x++) {
            Line line = new Line(0, 0, 0, GRID_HEIGHT * CELL_SIZE);
            line.setStroke(Color.GRAY);
            
            FXGL.entityBuilder()
                    .at(x * CELL_SIZE, 0)
                    .view(line)
                    .buildAndAttach();
        }

        for (int y = 0; y <= GRID_HEIGHT; y++) {
            Line line = new Line(0, 0, GRID_WIDTH * CELL_SIZE, 0);
            line.setStroke(Color.GRAY);
            
            FXGL.entityBuilder()
                    .at(0, y * CELL_SIZE)
                    .view(line)
                    .buildAndAttach();
        }
    }

    private void initSidebar() {
        sidebar = new VBox(20);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: white;");
        sidebar.setPrefWidth(SIDEBAR_WIDTH);
        
        // Create guest list label with larger font
        Label guestListLabel = new Label("Guest List");
        guestListLabel.setStyle("-fx-font-size: 42px; -fx-underline: true; -fx-cursor: hand;");
        guestListLabel.setOnMouseClicked(e -> {
            GuestListWindow guestListWindow = new GuestListWindow();
            guestListWindow.show();
        });
        
        // Create buttons for each furniture type
        StackPane chairButton = createImageButton("Chair", FurnitureType.CHAIR, "/com/btp/layout/images/chairIcon.png");
        StackPane tableButton = createImageButton("Table", FurnitureType.TABLE, "/com/btp/layout/images/tableIcon.png");
        StackPane wallButton = createImageButton("Wall", FurnitureType.WALL, "/com/btp/layout/images/wallIcon.png");
        StackPane floorButton = createImageButton("Floor", FurnitureType.FLOOR, "/com/btp/layout/images/floorIcon.png");
        
        // Add edit tool button
        StackPane editButton = createImageButton("Edit Tool", FurnitureType.EDIT, "/com/btp/layout/images/edit.png");
        
        
        // Add erase tool button
        StackPane eraseButton = createImageButton("Erase Tool", FurnitureType.ERASE, "/com/btp/layout/images/eraser.png");
        
        
        sidebar.getChildren().addAll(
            guestListLabel,
            chairButton, tableButton, wallButton, floorButton,
            editButton, eraseButton
        );
        
        // Add sidebar to UI with high Z-index to keep it on top
        FXGL.addUINode(sidebar, FXGL.getAppWidth() - SIDEBAR_WIDTH, 0);
    }
    
    private StackPane createImageButton(String text, FurnitureType type, String imagePath) {
        StackPane button = new StackPane();
        button.setPrefWidth(80);
        button.setPrefHeight(80);
        button.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-border-width: 2;");
        
        // Create image view
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            // If image fails to load, create a colored rectangle as fallback
            Rectangle fallback = new Rectangle(60, 60);
            fallback.setFill(type == FurnitureType.EDIT ? Color.LIGHTBLUE : 
                            type == FurnitureType.ERASE ? Color.LIGHTPINK :
                            Color.GRAY);
            button.getChildren().add(fallback);
        }
        
        // Add tooltip
        Tooltip tooltip = new Tooltip(text);
        Tooltip.install(button, tooltip);
        
        // Add click handler
        button.setOnMouseClicked(e -> selectButton(button, type));
        
        // Add to list of tool buttons
        toolButtons.add(button);
        
        button.getChildren().add(imageView);
        return button;
    }
    
    private void selectButton(StackPane button, FurnitureType type) {
        // Reset previous selection
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-border-width: 2;");
        }
        
        // Set new selection
        selectedButton = button;
        button.setStyle("-fx-background-color: white; -fx-border-color: #0078D7; -fx-border-width: 2;");
        
        // Update current furniture type
        FurnitureFactory.setCurrentType(type);
    }

    private void initHighlight() {
        Rectangle highlight = new Rectangle(CELL_SIZE, CELL_SIZE);
        highlight.setFill(Color.BLUE);
        highlight.setOpacity(0.1);
        
        highlightEntity = FXGL.entityBuilder()
                .view(highlight)
                .zIndex(1000)
                .buildAndAttach();
        highlightEntity.setVisible(false);
    }

    private void updateHighlight(double mouseX, double mouseY) {
        // Don't show highlight in sidebar area
        if (mouseX > GRID_WIDTH * CELL_SIZE) {
            highlightEntity.setVisible(false);
            return;
        }
        
        int gridX = (int) (mouseX / CELL_SIZE);
        int gridY = (int) (mouseY / CELL_SIZE);
        
        // Check if position is within grid bounds
        if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
            highlightEntity.setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
            highlightEntity.setVisible(true);
        } else {
            highlightEntity.setVisible(false);
        }
    }

    private void initTooltip() {
        tooltipPopup = new Popup();
        tooltipLabel = new Label();
        tooltipLabel.setStyle(
            "-fx-background-color: white;" +
            "-fx-padding: 5;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-font-size: 14px;"
        );
        tooltipPopup.getContent().add(tooltipLabel);
        
        tooltipDelay = new PauseTransition(Duration.millis(40));
        tooltipDelay.setOnFinished(e -> showTooltip());
    }

    private void updateTooltip(double mouseX, double mouseY) {
        // Only show tooltip when edit tool is selected
        if (FurnitureFactory.getCurrentType() != FurnitureType.EDIT) {
            hideTooltip();
            return;
        }

        // Find furniture at current position
        Entity hoveredEntity = findFurnitureAt(mouseX, mouseY);
        
        if (hoveredEntity != lastHoveredFurniture) {
            lastHoveredFurniture = hoveredEntity;
            if (hoveredEntity != null) {
                // Use scene coordinates for tooltip placement with right offset
                tooltipPopup.setX(lastMouseSceneX + 200); // Add 200 pixel offset to the right
                tooltipPopup.setY(lastMouseSceneY);
                
                // Start delay before showing tooltip
                tooltipDelay.playFromStart();
            } else {
                hideTooltip();
            }
        }
    }

    private void showTooltip() {
        if (lastHoveredFurniture == null) return;
        
        Furniture furniture = (Furniture) lastHoveredFurniture;
        String guestInfo;
        
        String singleGuest = furniture.getAssignedGuest();
        List<String> multipleGuests = furniture.getAssignedGuests();
        
        if (singleGuest != null) {
            guestInfo = "Assigned Guest: " + singleGuest;
        } else if (multipleGuests != null && !multipleGuests.isEmpty()) {
            guestInfo = "Assigned Guests:\n" + String.join("\n", multipleGuests);
        } else {
            guestInfo = "No guests assigned";
        }
        
        tooltipLabel.setText(guestInfo);
    
        // Force layout to get accurate dimensions
        tooltipLabel.applyCss();
        tooltipLabel.layout();
    
        // Position tooltip above the cursor
        double tooltipX = tooltipPopup.getX();
        double tooltipY = tooltipPopup.getY() - tooltipLabel.getHeight() - 5; // 5px gap above cursor
    
        // Show tooltip relative to the scene root
        tooltipPopup.show(FXGL.getGameScene().getRoot(), tooltipX, tooltipY);
    }

    private void hideTooltip() {
        tooltipDelay.stop();
        tooltipPopup.hide();
        lastHoveredFurniture = null;
    }

    private Entity findFurnitureAt(double x, double y) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> {
                    var bbox = e.getBoundingBoxComponent();
                    if (bbox == null) return false;

                    double minX = e.getX();
                    double minY = e.getY();
                    double maxX = minX + bbox.getWidth();
                    double maxY = minY + bbox.getHeight();

                    return x >= minX && x <= maxX && y >= minY && y <= maxY;
                })
                .reduce((first, second) -> second) // Get the topmost entity
                .orElse(null);
    }

    private void initMouseInput() {
        // Add mouse move handler for highlight and tooltip
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            var mousePosition = FXGL.getInput().getMousePositionWorld();
            lastMouseSceneX = e.getSceneX();
            lastMouseSceneY = e.getSceneY();
            updateHighlight(mousePosition.getX(), mousePosition.getY());
            updateTooltip(mousePosition.getX(), mousePosition.getY());
        });
        
        // Add mouse drag handler for highlight
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            var mousePosition = FXGL.getInput().getMousePositionWorld();
            lastMouseSceneX = e.getSceneX();
            lastMouseSceneY = e.getSceneY();
            updateHighlight(mousePosition.getX(), mousePosition.getY());
            updateTooltip(mousePosition.getX(), mousePosition.getY());
        });

        // Reset last placement position when mouse button is released
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                lastPlacementX = -1;
                lastPlacementY = -1;
            }
        });

        // Handle mouse movement while button is held
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                var mousePosition = FXGL.getInput().getMousePositionWorld();
                handleMouseAtPosition(mousePosition.getX(), mousePosition.getY());
            }
        });

        // Handle initial click
        FXGL.onBtnDown(MouseButton.PRIMARY, () -> {
            var mousePosition = FXGL.getInput().getMousePositionWorld();
            handleMouseAtPosition(mousePosition.getX(), mousePosition.getY());
        });
    }

    private void handleMouseAtPosition(double x, double y) {
        // Get world-relative mouse position
        Point2D worldMouse = FXGL.getInput().getMousePositionWorld();
        
        // Don't handle clicks in sidebar area (convert world X to screen X for sidebar check)
        double screenX = worldMouse.getX() - FXGL.getGameScene().getViewport().getX();
        if (screenX >= FXGL.getAppWidth() - SIDEBAR_WIDTH) {
            return;
        }
        
        int gridX = (int) (worldMouse.getX() / CELL_SIZE);
        int gridY = (int) (worldMouse.getY() / CELL_SIZE);

        // Check if position is within grid bounds
        if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
            // Only place if we're at a new tile
            if (gridX != lastPlacementX || gridY != lastPlacementY) {
                if (FurnitureFactory.getCurrentType() == FurnitureType.EDIT) {
                    handleEditClick(worldMouse);
                } else if (FurnitureFactory.getCurrentType() == FurnitureType.ERASE) {
                    handleEraseClick(worldMouse);
                } else {
                    var furniture = FurnitureFactory.createFurniture(gridX, gridY);
                    if (furniture != null) {
                        FXGL.getGameWorld().addEntity(furniture);
                        // Update last placement position
                        lastPlacementX = gridX;
                        lastPlacementY = gridY;
                    }
                }
            }
        }
    }

    private void handleEditClick(Point2D point) {
        System.out.println("Click at: " + point);
        
        var entities = FXGL.getGameWorld().getEntities();
        System.out.println("Total entities: " + entities.size());
        
        entities.stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> {
                    var bbox = e.getBoundingBoxComponent();
                    if (bbox == null) {
                        return false;
                    }

                    double minX = e.getX();
                    double minY = e.getY();
                    double maxX = minX + bbox.getWidth();
                    double maxY = minY + bbox.getHeight();

                    return point.getX() >= minX && point.getX() <= maxX &&
                           point.getY() >= minY && point.getY() <= maxY;
                })
                .reduce((first, second) -> second)
                .ifPresent(e -> {
                    System.out.println("Found furniture at: " + e.getPosition());
                    Furniture furniture = (Furniture) e;
                    EditWindow editWindow = new EditWindow(furniture);
                    editWindow.show();
                });
    }

    private void handleEraseClick(Point2D point) {
        System.out.println("Erase click at: " + point);
        
        var entities = FXGL.getGameWorld().getEntities();
        System.out.println("Total entities: " + entities.size());
        
        entities.stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> {
                    var bbox = e.getBoundingBoxComponent();
                    if (bbox == null) {
                        return false;
                    }

                    double minX = e.getX();
                    double minY = e.getY();
                    double maxX = minX + bbox.getWidth();
                    double maxY = minY + bbox.getHeight();

                    return point.getX() >= minX && point.getX() <= maxX &&
                           point.getY() >= minY && point.getY() <= maxY;
                })
                .reduce((first, second) -> second)
                .ifPresent(e -> {
                    System.out.println("Erasing furniture at: " + e.getPosition());
                    Furniture furniture = (Furniture) e;
                    
                    // If the furniture has assigned guests, unassign them
                    String singleGuest = furniture.getAssignedGuest();
                    List<String> multipleGuests = furniture.getAssignedGuests();
                    
                    GuestList guestList = GuestList.getInstance();
                    if (singleGuest != null) {
                        guestList.unassignFromChair(singleGuest);
                    }
                    if (multipleGuests != null) {
                        for (String guest : multipleGuests) {
                            guestList.unassignFromTable(guest);
                        }
                    }
                    
                    // Remove the entity
                    FXGL.getGameWorld().removeEntity(e);
                });
    }

    public static void main(String[] args) {
        launch(args);
    }
} 