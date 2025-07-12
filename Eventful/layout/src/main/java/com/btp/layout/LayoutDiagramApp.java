package com.btp.layout;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.core.math.FXGLMath;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.geometry.Orientation;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Tooltip;
import javafx.stage.Popup;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import com.btp.layout.model.Furniture;
import com.btp.layout.model.FurnitureFactory;
import com.btp.layout.model.FurnitureFactory.FurnitureType;
import com.btp.layout.model.GuestList;
import com.btp.layout.model.FurnitureStyle;
import com.btp.layout.model.commands.CommandManager;
import com.btp.layout.model.commands.PlaceFurnitureCommand;
import com.btp.layout.model.commands.EraseCommand;
import com.btp.layout.model.commands.MoveFurnitureCommand;
import com.btp.layout.ui.EditWindow;
import com.btp.layout.ui.GuestListWindow;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.Cursor;
import javafx.scene.layout.Region;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.btp.layout.model.LayoutManager;

public class LayoutDiagramApp extends GameApplication {

    private static final int CELL_SIZE = 80;
    private static final int GRID_WIDTH = 60;
    private static final int GRID_HEIGHT = 60;
    private static final int BOTTOM_BAR_HEIGHT = 100; // Increased by 20 pixels
    private static final int MENU_BAR_HEIGHT = 20; // Height for the menu bar
    
    // World size (total size of the pannable area)
    private static final int WORLD_WIDTH = GRID_WIDTH * CELL_SIZE;
    private static final int WORLD_HEIGHT = GRID_HEIGHT * CELL_SIZE;
    
    // Window size (visible area)
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    // Context menu
    private VBox contextMenu;
    private boolean isContextMenuOpen = false;

    // Scrollbars
    private ScrollBar horizontalScrollBar;
    private ScrollBar verticalScrollBar;
    private boolean isUpdatingScrollBars = false;
    
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
    private HBox sidebar;

    private static final double MIN_SCALE = 0.3;
    private static final double MAX_SCALE = 2.0;
    private static final double ZOOM_FACTOR = 0.1;

    private long lastUpdateTime = 0;
    private static final long UPDATE_DELAY = 10; // 10ms delay

    private Entity lastClickedFurniture = null;
    private Point2D initialDragPoint = null;
    private int initialGridX = -1;
    private int initialGridY = -1;
    private boolean isRotating = false;
    private CommandManager commandManager;
    private int lastRotation = 0;

    private List<Entity> gridLines = new ArrayList<>();
    private int lastEraseX = -1;
    private int lastEraseY = -1;

    private Rectangle selectionBox;
    private Point2D selectionStart;
    private boolean isSelecting = false;

    // Add fields for viewport drag
    private boolean isDraggingViewport = false;
    private double lastDragX = 0;
    private double lastDragY = 0;

    private javafx.scene.layout.HBox styleButtonBox = null;
    private StackPane selectedStyleButton = null;
    private MenuBar menuBar = null;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Layout Diagram");
        settings.setVersion("1.0");
        settings.setPreserveResizeRatio(true); // Keep aspect ratio and letterbox
        settings.setFullScreenAllowed(false);  // Prevent fullscreen
        settings.setManualResizeEnabled(false); // Prevent manual resizing
        commandManager = CommandManager.getInstance();
    }

    private void initMenuBar() {
        menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        
        // Save menu item
        MenuItem saveItem = new MenuItem("Save Layout");
        saveItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Layout");
            saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Layout files (*.layout)", "*.layout")
            );
            File file = fileChooser.showSaveDialog(FXGL.getGameScene().getRoot().getScene().getWindow());
            if (file != null) {
                try {
                    LayoutManager.getInstance().saveLayout(file.getAbsolutePath());
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to save layout");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });
        
        // Load menu item
        MenuItem loadItem = new MenuItem("Load Layout");
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        loadItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Layout");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Layout files (*.layout)", "*.layout")
            );
            File file = fileChooser.showOpenDialog(FXGL.getGameScene().getRoot().getScene().getWindow());
            if (file != null) {
                try {
                    LayoutManager.getInstance().loadLayout(file.getAbsolutePath());
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to load layout");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });
        
        // Save image menu item
        MenuItem saveImageItem = new MenuItem("Export");
        saveImageItem.setOnAction(e -> saveLayoutImage());
        
        fileMenu.getItems().addAll(saveItem, loadItem, new SeparatorMenuItem(), saveImageItem);
        
        // Add Guest List menu
        Menu guestMenu = new Menu("Guest List");
        MenuItem guestListItem = new MenuItem("Open Guest List");
        guestListItem.setOnAction(e -> {
            GuestListWindow guestListWindow = new GuestListWindow();
            guestListWindow.showAndWait();
        });
        guestMenu.getItems().add(guestListItem);
        
        menuBar.getMenus().addAll(fileMenu, guestMenu);
        
        // Style the menu bar and its components
        menuBar.setPrefWidth(FXGL.getAppWidth());
        menuBar.setPrefHeight(MENU_BAR_HEIGHT);
        
        // Apply CSS styling
        menuBar.setStyle(
            "-fx-background-color: #c7aef7;" +
            "-fx-text-fill: white;"
        );
        
        // Add CSS stylesheet for menu components
        FXGL.getGameScene().getRoot().setStyle(
            ".menu-bar { -fx-background-color: #c7aef7; }" +
            ".menu-bar .menu { -fx-text-fill: white; }" +
            ".menu-bar .menu:showing { -fx-background-color: #a259e6; }" +
            ".menu-bar .menu-item { -fx-background-color: #c7aef7; -fx-text-fill: white; }" +
            ".menu-bar .menu-item:hover { -fx-background-color: #a259e6; -fx-text-fill: white; }" +
            ".context-menu { -fx-background-color: #c7aef7; -fx-text-fill: white; }"
        );
        
        FXGL.addUINode(menuBar, 0, 0);
    }

    @Override
    protected void initGame() {
        FXGL.getGameScene().setBackgroundColor(Color.WHITE);
        FXGL.getGameScene().getRoot().setCursor(Cursor.DEFAULT);
        initMenuBar();
        initGrid();
        initSidebar();
        initMouseInput();
        initHighlight();
        initTooltip();
        initZoomAndScroll();
        initSelectionBox();
        initScrollbars();
        initContextMenu();
    }

    private void initSelectionBox() {
        selectionBox = new Rectangle();
        selectionBox.setFill(Color.TRANSPARENT);
        selectionBox.setStroke(Color.BLUE);
        selectionBox.setStrokeWidth(1);
        selectionBox.getStrokeDashArray().addAll(5.0, 5.0);
        selectionBox.setVisible(false);
        FXGL.addUINode(selectionBox);
    }

    private void initScrollbars() {
        // Create horizontal scrollbar
        horizontalScrollBar = new ScrollBar();
        horizontalScrollBar.setOrientation(Orientation.HORIZONTAL);
        horizontalScrollBar.setPrefWidth(FXGL.getAppWidth() - 17); // Leave space for vertical scrollbar
        horizontalScrollBar.setMin(0);
        horizontalScrollBar.setUnitIncrement(CELL_SIZE);
        horizontalScrollBar.setBlockIncrement(CELL_SIZE * 5);
        
        
        // Create vertical scrollbar
        verticalScrollBar = new ScrollBar();
        verticalScrollBar.setOrientation(Orientation.VERTICAL);
        verticalScrollBar.setPrefHeight(FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT - MENU_BAR_HEIGHT - 22); // Adjust for menu bar and horizontal scrollbar
        verticalScrollBar.setMin(0);
        verticalScrollBar.setUnitIncrement(CELL_SIZE);
        verticalScrollBar.setBlockIncrement(CELL_SIZE * 5);
        

        // Add scrollbars to UI
        FXGL.addUINode(horizontalScrollBar, 0, FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT - 17);
        FXGL.addUINode(verticalScrollBar, FXGL.getAppWidth() - 17, MENU_BAR_HEIGHT + 5);

        // Add listeners to update viewport when scrollbars change
        horizontalScrollBar.valueProperty().addListener((obs, old, newValue) -> {
            if (!isUpdatingScrollBars) {
        var viewport = FXGL.getGameScene().getViewport();
                viewport.setX(newValue.doubleValue());
            }
        });

        verticalScrollBar.valueProperty().addListener((obs, old, newValue) -> {
            if (!isUpdatingScrollBars) {
                var viewport = FXGL.getGameScene().getViewport();
                viewport.setY(newValue.doubleValue());
            }
        });

        // Initial update of scrollbar ranges
        updateScrollbarRanges();
    }

    private void updateScrollbarRanges() {
        var viewport = FXGL.getGameScene().getViewport();
        double currentZoom = viewport.getZoom();

        // Calculate max scroll ranges based on world size and current zoom
            double maxX = Math.max(0, WORLD_WIDTH - (FXGL.getAppWidth() / currentZoom));
            double maxY = Math.max(0, WORLD_HEIGHT - ((FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) / currentZoom));

        // Calculate visible amounts (thumb sizes)
        double visibleWidth = FXGL.getAppWidth() / currentZoom;
        double visibleHeight = (FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) / currentZoom;
        
        // Calculate proportions of visible area to total area
        double horizontalProportion = visibleWidth / WORLD_WIDTH;
        double verticalProportion = visibleHeight / WORLD_HEIGHT;

        isUpdatingScrollBars = true;
        
        // Update horizontal scrollbar
        horizontalScrollBar.setMax(maxX);
        horizontalScrollBar.setValue(viewport.getX());
        horizontalScrollBar.setVisibleAmount(maxX * horizontalProportion);
        
        // Update vertical scrollbar
        verticalScrollBar.setMax(maxY);
        verticalScrollBar.setValue(viewport.getY());
        verticalScrollBar.setVisibleAmount(maxY * verticalProportion);
        
        isUpdatingScrollBars = false;
            }

    @Override
    protected void onUpdate(double tpf) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime < UPDATE_DELAY) {
            return;
        }
        lastUpdateTime = currentTime;

        // Keep sidebar at the bottom of the screen
        if (sidebar != null) {
            sidebar.setTranslateX(0);
            sidebar.setTranslateY(FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT);
        }

        // Update scrollbar ranges when zoom changes
        updateScrollbarRanges();
    }

    private void initGrid() {
        // Create a container entity for the grid
        var gridEntity = FXGL.entityBuilder()
                .buildAndAttach();

        // Draw grid lines with higher z-order to appear on top
        for (int x = 0; x <= GRID_WIDTH; x++) {
            Line line = new Line(0, 0, 0, GRID_HEIGHT * CELL_SIZE);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1.0); // Ensure consistent line width
            
            Entity gridLine = FXGL.entityBuilder()
                    .at(x * CELL_SIZE, 0)
                    .view(line)
                    .zIndex(1000) // Set high z-index to render on top of furniture
                    .buildAndAttach();
            gridLines.add(gridLine);
        }

        for (int y = 0; y <= GRID_HEIGHT; y++) {
            Line line = new Line(0, 0, GRID_WIDTH * CELL_SIZE, 0);
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1.0); // Ensure consistent line width
            
            Entity gridLine = FXGL.entityBuilder()
                    .at(0, y * CELL_SIZE)
                    .view(line)
                    .zIndex(1000) // Set high z-index to render on top of furniture
                    .buildAndAttach();
            gridLines.add(gridLine);
        }
    }

    private void initSidebar() {
        sidebar = new HBox();
        sidebar.setAlignment(Pos.CENTER_LEFT);
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #c7aef7;"); // Light purple background
        sidebar.setPrefHeight(BOTTOM_BAR_HEIGHT);
        sidebar.setPrefWidth(WINDOW_WIDTH);
        sidebar.setSpacing(20); // Spacing between sections

        // Tools Section
        VBox toolsSection = new VBox(5);
        Label toolsLabel = new Label("Tools");
        toolsLabel.setStyle("-fx-font-size: 16px;");
        var labelBox = new HBox();
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().add(toolsLabel);
        var toolsHBox = new HBox(10);
        toolsHBox.setAlignment(Pos.CENTER_LEFT);
        toolsHBox.setPadding(new Insets(5, 0, 5, 0));

        // Create tool buttons
        StackPane editButton = createImageButton("Edit Tool", FurnitureType.EDIT, "/com/btp/layout/images/edit.png");
        StackPane eraseButton = createImageButton("Erase Tool", FurnitureType.ERASE, "/com/btp/layout/images/eraser.png");
        toolsHBox.getChildren().addAll(editButton, eraseButton);
        toolsSection.getChildren().addAll(labelBox, toolsHBox);

        // Furniture Section
        VBox furnitureSection = new VBox(5);
        Label furnitureLabel = new Label("Furniture");
        furnitureLabel.setStyle("-fx-font-size: 16px;");
        var furnitureLabelBox = new HBox();
        furnitureLabelBox.setAlignment(Pos.CENTER);
        furnitureLabelBox.getChildren().add(furnitureLabel);
        var furnitureHBox = new HBox(10);
        furnitureHBox.setAlignment(Pos.CENTER_LEFT);
        furnitureHBox.setPadding(new Insets(5, 0, 5, 0));

        // Create furniture buttons
        StackPane chairButton = createImageButton("Chair", FurnitureType.CHAIR, "/com/btp/layout/images/chairIcon.png");
        StackPane tableButton = createImageButton("Table", FurnitureType.TABLE, "/com/btp/layout/images/tableIcon.png");
        StackPane wallButton = createImageButton("Wall", FurnitureType.WALL, "/com/btp/layout/images/wallIcon.png");
        StackPane floorButton = createImageButton("Floor", FurnitureType.FLOOR, "/com/btp/layout/images/floorIcon.png");
        furnitureHBox.getChildren().addAll(chairButton, tableButton, wallButton, floorButton);
        furnitureSection.getChildren().addAll(furnitureLabelBox, furnitureHBox);

        // Styles Section
        VBox stylesSection = new VBox(5);
        Label stylesLabel = new Label("Styles");
        stylesLabel.setStyle("-fx-font-size: 16px;");
        var stylesLabelBox = new HBox();
        stylesLabelBox.setAlignment(Pos.CENTER);
        stylesLabelBox.getChildren().add(stylesLabel);
        styleButtonBox = new javafx.scene.layout.HBox(10);
        styleButtonBox.setAlignment(Pos.CENTER_LEFT);
        styleButtonBox.setPadding(new Insets(5, 0, 5, 0));
        stylesSection.getChildren().addAll(stylesLabelBox, styleButtonBox);
        stylesSection.setVisible(false);
        stylesSection.setManaged(false);

        // Create separators
        Separator sep1 = new Separator(Orientation.VERTICAL);
        Separator sep2 = new Separator(Orientation.VERTICAL);
        
        // Add all sections to sidebar
        sidebar.getChildren().addAll(
            toolsSection, sep1,
            furnitureSection, sep2,
            stylesSection
        );
        
        // Add sidebar to UI at the bottom
        FXGL.addUINode(sidebar, 0, WINDOW_HEIGHT - BOTTOM_BAR_HEIGHT);
    }
    
    private StackPane createImageButton(String text, FurnitureType type, String imagePath) {
        StackPane button = new StackPane();
        button.setPrefWidth(30);
        button.setPrefHeight(30);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
        
        // Create image view
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
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
        // Deselect if already selected
        if (selectedButton == button) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
            selectedButton = null;
            FurnitureFactory.setCurrentType(null); // Use null or a NONE type to indicate no tool
            FurnitureStyle.setCurrentStyle(null); // Clear style selection as well
            // Clear style buttons but keep the container
            if (styleButtonBox != null) {
                styleButtonBox.getChildren().clear();
            }
            return;
        }
        // Reset previous selection
        if (selectedButton != null) {
            selectedButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
        }
        
        // Set new selection
        selectedButton = button;
        button.setStyle("-fx-background-color: transparent; -fx-border-color: #a259e6; -fx-border-width: 2;");
        
        // Update current furniture type
        FurnitureFactory.setCurrentType(type);

        // Update style buttons based on type
        VBox stylesSection = (VBox) styleButtonBox.getParent();
        if (type == FurnitureType.CHAIR || type == FurnitureType.TABLE || type == FurnitureType.WALL || type == FurnitureType.FLOOR) {
            updateStyleButtons(type, styleButtonBox);
            stylesSection.setVisible(true);
            stylesSection.setManaged(true);
        } else {
            // Hide style buttons for edit/erase
            styleButtonBox.getChildren().clear();
            stylesSection.setVisible(false);
            stylesSection.setManaged(false);
        }
    }

    private void updateStyleButtons(FurnitureType type, javafx.scene.layout.HBox styleButtonBox) {
        // Clear existing style buttons
        styleButtonBox.getChildren().clear();

        // Don't show styles for edit and erase tools
        if (type == FurnitureType.EDIT || type == FurnitureType.ERASE) {
            return;
        }

        // Get available styles for the selected type
        List<String> styles = FurnitureStyle.getStylesForType(type);
        
        // Create a button for each style
        StackPane firstStyleButton = null;
        for (String style : styles) {
            StackPane styleButton = createStyleButton(style, type);
            styleButtonBox.getChildren().add(styleButton);
            if (firstStyleButton == null) {
                firstStyleButton = styleButton;
            }
        }
        // Auto-select the first style button
        if (firstStyleButton != null) {
            selectStyleButton(firstStyleButton, styles.get(0));
        }
    }

    private StackPane createStyleButton(String imagePath, FurnitureType type) {
        StackPane button = new StackPane();
        button.setPrefWidth(30);
        button.setPrefHeight(30);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
        
        // Create image view
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            imageView.setImage(image);
            imageView.setFitWidth(30);
            imageView.setFitHeight(30);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            // If image fails to load, create a colored rectangle as fallback
            Rectangle fallback = new Rectangle(60, 60);
            fallback.setFill(Color.GRAY);
            button.getChildren().add(fallback);
        }
        
        // Add click handler
        button.setOnMouseClicked(e -> {
            selectStyleButton(button, imagePath);
        });
        
        button.getChildren().add(imageView);
        return button;
    }

    private void selectStyleButton(StackPane button, String stylePath) {
        // Deselect previous
        if (selectedStyleButton != null) {
            selectedStyleButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-border-width: 2;");
        }
        selectedStyleButton = button;
        button.setStyle("-fx-background-color: transparent; -fx-border-color: #a259e6; -fx-border-width: 2;");
        FurnitureStyle.setCurrentStyle(stylePath);
    }

    private void initHighlight() {
        Rectangle highlight = new Rectangle(CELL_SIZE, CELL_SIZE);
        highlight.setFill(Color.web("#e6d6fa")); // Light purple
        highlight.setOpacity(0.5);
        
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
        
        // Convert mouse coordinates to grid coordinates
        int gridX = (int) (mouseX / CELL_SIZE);
        int gridY = (int) (mouseY / CELL_SIZE);
        
        // Check if position is within grid bounds
        if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
            // Scale the highlight rectangle based on current zoom
            double currentZoom = FXGL.getGameScene().getViewport().getZoom();
            Rectangle highlight = (Rectangle) highlightEntity.getViewComponent().getChildren().get(0);
            highlight.setWidth(CELL_SIZE);
            highlight.setHeight(CELL_SIZE);
            
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
        // Only show tooltip when edit tool is selected and context menu is closed
        if (FurnitureFactory.getCurrentType() != FurnitureType.EDIT || isContextMenuOpen) {
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

    private void handleEditClick(Point2D point, MouseEvent e) {
        // Check if click is in bottom bar area
        var viewport = FXGL.getGameScene().getViewport();
        double screenY = (point.getY() - viewport.getY()) * viewport.getZoom();
        if (screenY >= FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) {
            return;  // Don't handle selection in bottom bar area
        }

        var entities = FXGL.getGameWorld().getEntities();
        
        var clickedFurniture = entities.stream()
                .filter(e2 -> e2 instanceof Furniture)
                .filter(e2 -> {
                    var bbox = e2.getBoundingBoxComponent();
                    if (bbox == null) {
                        return false;
                    }

                    double minX = e2.getX();
                    double minY = e2.getY();
                    double maxX = minX + bbox.getWidth();
                    double maxY = minY + bbox.getHeight();

                    return point.getX() >= minX && point.getX() <= maxX &&
                           point.getY() >= minY && point.getY() <= maxY;
                })
                .reduce((first, second) -> second)
                .orElse(null);

        // Reset any previous selections
        if (isSelecting) {
            isSelecting = false;
            selectionBox.setVisible(false);
        }
        
        // Reset opacity of all furniture
        FXGL.getGameWorld().getEntities().stream()
                .filter(e2 -> e2 instanceof Furniture)
                .forEach(e2 -> ((Furniture) e2).getViewComponent().setOpacity(1.0));

        if (clickedFurniture != null) {
            lastClickedFurniture = clickedFurniture;
            initialDragPoint = point;
            // Store initial grid position
            Furniture furniture = (Furniture) clickedFurniture;
            initialGridX = furniture.getGridX();
            initialGridY = furniture.getGridY();
            // Highlight the selected furniture by changing its opacity
            furniture.getViewComponent().setOpacity(0.7);
        } else {
            // Start selection box using world coordinates
            selectionStart = point;
            isSelecting = true;
            
            // Convert world coordinates to screen coordinates for the selection box
            double screenX = (point.getX() - viewport.getX()) * viewport.getZoom();
            selectionBox.setX(screenX);
            selectionBox.setY(screenY);
            selectionBox.setWidth(0);
            selectionBox.setHeight(0);
            selectionBox.setVisible(true);
        }
    }

    private void updateSelectionBox(Point2D currentPoint) {
        if (!isSelecting) return;

        var viewport = FXGL.getGameScene().getViewport();
        
        // Don't update if we're in the bottom bar area
        double screenY = (currentPoint.getY() - viewport.getY()) * viewport.getZoom();
        if (screenY >= FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) {
            return;
        }
        
        // Convert both start and current points to screen coordinates
        double startScreenX = (selectionStart.getX() - viewport.getX()) * viewport.getZoom();
        double startScreenY = (selectionStart.getY() - viewport.getY()) * viewport.getZoom();
        double currentScreenX = (currentPoint.getX() - viewport.getX()) * viewport.getZoom();
        double currentScreenY = Math.min(screenY, FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT - 1);  // Clamp to above bottom bar

        // Calculate top-left corner and dimensions in screen coordinates
        double x = Math.min(startScreenX, currentScreenX);
        double y = Math.min(startScreenY, currentScreenY);
        double width = Math.abs(currentScreenX - startScreenX);
        double height = Math.abs(currentScreenY - startScreenY);

        // Update selection box with screen coordinates
        selectionBox.setX(x);
        selectionBox.setY(y);
        selectionBox.setWidth(width);
        selectionBox.setHeight(height);
    }

    private void handleSelectionComplete() {
        if (!isSelecting) return;

        var viewport = FXGL.getGameScene().getViewport();
        
        // Convert selection box screen coordinates back to world coordinates
        double worldX = selectionBox.getX() / viewport.getZoom() + viewport.getX();
        double worldY = selectionBox.getY() / viewport.getZoom() + viewport.getY();
        double worldWidth = selectionBox.getWidth() / viewport.getZoom();
        double worldHeight = selectionBox.getHeight() / viewport.getZoom();

        // Get all furniture within the selection box using world coordinates
        var selectedFurniture = FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .filter(e -> {
                    var bbox = e.getBoundingBoxComponent();
                    if (bbox == null) return false;

                    double entityX = e.getX();
                    double entityY = e.getY();
                    double entityMaxX = entityX + bbox.getWidth();
                    double entityMaxY = entityY + bbox.getHeight();

                    return entityX < worldX + worldWidth &&
                           entityMaxX > worldX &&
                           entityY < worldY + worldHeight &&
                           entityMaxY > worldY;
                })
                .collect(Collectors.toList());

        // Highlight selected furniture
        selectedFurniture.forEach(e -> {
            if (e instanceof Furniture) {
                ((Furniture) e).getViewComponent().setOpacity(0.7);
            }
        });

        // Reset selection
        isSelecting = false;
        selectionBox.setVisible(false);
    }

    // Add mouse drag handler for edit mode
    private void handleEditDrag(Point2D mousePosition) {
        if (lastClickedFurniture != null && lastClickedFurniture instanceof Furniture && FurnitureFactory.getCurrentType() == FurnitureType.EDIT) {
            // Convert mouse position to grid coordinates
            int gridX = (int) (mousePosition.getX() / CELL_SIZE);
            int gridY = (int) (mousePosition.getY() / CELL_SIZE);
            
            // Check if the new position is within bounds and not occupied
            if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
                Furniture furniture = (Furniture) lastClickedFurniture;
                // Only move if the new cell is different and not occupied
                if (furniture.getGridX() != gridX || furniture.getGridY() != gridY) {
                    if (!isCellOccupied(gridX, gridY) || (gridX == furniture.getGridX() && gridY == furniture.getGridY())) {
                        // Just update the position during drag
                        furniture.setPosition(gridX * CELL_SIZE, gridY * CELL_SIZE);
                        furniture.setGridPosition(gridX, gridY);
                    }
                }
            }
        }
    }

    // Add mouse release handler for edit mode
    private void handleEditRelease() {
        if (lastClickedFurniture != null && lastClickedFurniture instanceof Furniture) {
            Furniture furniture = (Furniture) lastClickedFurniture;
            
            // Create and execute move command if the position changed
            if (furniture.getGridX() != initialGridX || furniture.getGridY() != initialGridY) {
                MoveFurnitureCommand command = new MoveFurnitureCommand(furniture, initialGridX, initialGridY, 
                    furniture.getGridX(), furniture.getGridY());
                commandManager.executeCommand(command);
            }
            
            // Reset the opacity of the furniture
            furniture.getViewComponent().setOpacity(1.0);
            lastClickedFurniture = null;
            initialDragPoint = null;
        }
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
        
        // Add mouse press handler
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                var mousePosition = FXGL.getInput().getMousePositionWorld();
        
            // Handle right click for context menu
            if (e.getButton() == MouseButton.SECONDARY) {
                var clickedEntity = FXGL.getGameWorld().getEntities()
                        .stream()
                        .filter(entity -> entity instanceof Furniture)
                        .filter(entity -> {
                            var bbox = entity.getBoundingBoxComponent();
                            if (bbox == null) return false;
        
                            double minX = entity.getX();
                            double minY = entity.getY();
                            double maxX = minX + bbox.getWidth();
                            double maxY = minY + bbox.getHeight();
        
                            return mousePosition.getX() >= minX && mousePosition.getX() <= maxX &&
                                   mousePosition.getY() >= minY && mousePosition.getY() <= maxY;
                        })
                        .reduce((first, second) -> second)
                        .orElse(null);
        
                if (clickedEntity instanceof Furniture furniture) {
                    switch (furniture.getFurnitureType()) {
                        case CHAIR, TABLE -> showContextMenu(furniture, e.getSceneX(), e.getSceneY());
                        default -> hideContextMenu();
                    }
                }
                return;
            }
            
            // Hide context menu on left click outside
            if (e.getButton() == MouseButton.PRIMARY && isContextMenuOpen) {
                if (!contextMenu.getBoundsInParent().contains(e.getSceneX(), e.getSceneY())) {
                    hideContextMenu();
                }
                return;
            }
            
            // Handle primary click based on current tool
            if (e.getButton() == MouseButton.PRIMARY) {
                var currentType = FurnitureFactory.getCurrentType();
                if (currentType == FurnitureType.EDIT) {
                    handleEditClick(mousePosition, e);
                } else if (currentType != FurnitureType.ERASE) {
                    // Check for existing furniture first
                lastClickedFurniture = findFurnitureAt(mousePosition.getX(), mousePosition.getY());
                    
                if (lastClickedFurniture != null && lastClickedFurniture instanceof Furniture) {
                        // If we found furniture, enable rotation
                        initialDragPoint = mousePosition;
                    isRotating = true;
                    } else {
                        // If no furniture found, try to place new furniture
                        handleMouseAtPosition(mousePosition.getX(), mousePosition.getY());
                    }
                } else {
                    handleMouseAtPosition(mousePosition.getX(), mousePosition.getY());
                }
            }
            // Add viewport drag start
            if (selectedButton == null && e.getButton() == MouseButton.PRIMARY) {
                // Only start drag if inside grid area
                if (mousePosition.getX() >= 0 && mousePosition.getX() < GRID_WIDTH * CELL_SIZE &&
                    mousePosition.getY() >= 0 && mousePosition.getY() < GRID_HEIGHT * CELL_SIZE) {
                    isDraggingViewport = true;
                    lastDragX = e.getSceneX();
                    lastDragY = e.getSceneY();
                }
            }
        });

        // Modify mouse drag handler
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.isSecondaryButtonDown() || isContextMenuOpen) {
                return;
            }
            
            var mousePosition = FXGL.getInput().getMousePositionWorld();
            lastMouseSceneX = e.getSceneX();
            lastMouseSceneY = e.getSceneY();
            
            var currentType = FurnitureFactory.getCurrentType();
            if (currentType == FurnitureType.EDIT) {
                if (isSelecting) {
                    updateSelectionBox(mousePosition);
                } else {
                    handleEditDrag(mousePosition);
                }
            } else if (isRotating && lastClickedFurniture instanceof Furniture && currentType != FurnitureType.ERASE) {
                // Handle rotation for all tools except edit and erase
                Furniture furniture = (Furniture) lastClickedFurniture;
                
                // Calculate angle between initial click and current position
                double dx = mousePosition.getX() - (furniture.getGridX() * CELL_SIZE + CELL_SIZE / 2);
                double dy = mousePosition.getY() - (furniture.getGridY() * CELL_SIZE + CELL_SIZE / 2);
                double angle = Math.toDegrees(Math.atan2(dy, dx)) + 180;
                
                // Snap to nearest 90 degrees
                int newRotation = (int) Math.round(angle / 90) % 4;
                
                // Update rotation directly
                if (newRotation != lastRotation) {
                    furniture.setRotation(newRotation);
                    lastRotation = newRotation;
                }
            } else if (currentType == FurnitureType.ERASE) {
                // Get grid coordinates for current mouse position
                int gridX = (int) (mousePosition.getX() / CELL_SIZE);
                int gridY = (int) (mousePosition.getY() / CELL_SIZE);
                
                // Only erase if we've moved to a new cell
                if (gridX != lastEraseX || gridY != lastEraseY) {
                    handleEraseClick(mousePosition);
                    lastEraseX = gridX;
                    lastEraseY = gridY;
                }
            } else {
                updateHighlight(mousePosition.getX(), mousePosition.getY());
                updateTooltip(mousePosition.getX(), mousePosition.getY());
            }
            // Handle viewport drag if no tool is selected
            if (selectedButton == null && isDraggingViewport && e.getButton() == MouseButton.PRIMARY) {
                double dx = e.getSceneX() - lastDragX;
                double dy = e.getSceneY() - lastDragY;
                var viewport = FXGL.getGameScene().getViewport();
                double newX = viewport.getX() - dx / viewport.getZoom();
                double newY = viewport.getY() - dy / viewport.getZoom();
                // Clamp to grid area
                newX = Math.max(0, Math.min(newX, GRID_WIDTH * CELL_SIZE - FXGL.getAppWidth() / viewport.getZoom()));
                newY = Math.max(0, Math.min(newY, GRID_HEIGHT * CELL_SIZE - FXGL.getAppHeight() / viewport.getZoom()));
                viewport.setX(newX);
                viewport.setY(newY);
                lastDragX = e.getSceneX();
                lastDragY = e.getSceneY();
                return;
            }
        });

        // Modify mouse released handler to reset erase tracking
        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (FurnitureFactory.getCurrentType() == FurnitureType.EDIT) {
                    if (isSelecting) {
                        handleSelectionComplete();
                    } else {
                        handleEditRelease();
                    }
                }
                lastPlacementX = -1;
                lastPlacementY = -1;
                lastClickedFurniture = null;
                initialDragPoint = null;
                initialGridX = -1;
                initialGridY = -1;
                isRotating = false;
                lastRotation = 0;
                lastEraseX = -1;  // Reset erase position tracking
                lastEraseY = -1;
                isDraggingViewport = false;
            }
        });
    }

    private boolean isCellOccupied(int gridX, int gridY) {
        return FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .anyMatch(e -> {
                    Furniture furniture = (Furniture) e;
                    return furniture.getGridX() == gridX && furniture.getGridY() == gridY && e != lastClickedFurniture;
                });
    }

    private void handleMouseAtPosition(double x, double y) {
        // Get world-relative mouse position
        Point2D worldMouse = FXGL.getInput().getMousePositionWorld();
        
        // Don't handle clicks in bottom bar area
        var viewport = FXGL.getGameScene().getViewport();
        double screenY = (worldMouse.getY() - viewport.getY()) * viewport.getZoom();
        if (screenY >= FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) {
            return;
        }
        
        // Convert world coordinates to grid coordinates
        int gridX = (int) (worldMouse.getX() / CELL_SIZE);
        int gridY = (int) (worldMouse.getY() / CELL_SIZE);

        // Check if position is within grid bounds
        if (gridX >= 0 && gridX < GRID_WIDTH && gridY >= 0 && gridY < GRID_HEIGHT) {
            // Only place furniture if both a tool and a style are selected
            if (selectedButton != null && FurnitureStyle.getCurrentStyle() != null) {
                if (FurnitureFactory.getCurrentType() == FurnitureType.EDIT) {
                    handleEditClick(worldMouse, null); // Pass null for MouseEvent
                } else if (FurnitureFactory.getCurrentType() == FurnitureType.ERASE) {
                    handleEraseClick(worldMouse);
                } else {
                    // Check if the cell is already occupied
                    if (!isCellOccupied(gridX, gridY)) {
                        var furniture = FurnitureFactory.createFurniture(gridX, gridY);
                        if (furniture != null) {
                            // Create and execute the placement command
                            PlaceFurnitureCommand command = new PlaceFurnitureCommand(furniture);
                            commandManager.executeCommand(command);
                            // Enable rotation for the newly placed furniture
                            lastClickedFurniture = furniture;
                            initialDragPoint = worldMouse;
                            isRotating = true;
                        }
                    }
                }
            }
            // If not, do nothing (viewport drag is handled elsewhere)
        }
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
                    
                    // Create and execute the erase command
                    EraseCommand command = new EraseCommand(furniture);
                    commandManager.executeCommand(command);
                });
    }

    private void handleUndo() {
        if (commandManager.canUndo()) {
            commandManager.undo();
        }
    }

    private void initZoomAndScroll() {
        FXGL.getInput().addEventHandler(ScrollEvent.SCROLL, e -> {
            if (e.isControlDown()) {                    
            var viewport = FXGL.getGameScene().getViewport();
            float currentScale = (float) viewport.getZoom();
    
            // Calculate zoom direction
            float zoomChange = e.getDeltaY() > 0 ? (float) ZOOM_FACTOR : -(float) ZOOM_FACTOR;
            float newScale = FXGLMath.clamp(currentScale + zoomChange, (float) MIN_SCALE, (float) MAX_SCALE);
    
            if (newScale != currentScale) {
                // Mouse position relative to viewport
                double mouseX = e.getSceneX();
                double mouseY = e.getSceneY();
    
                // Convert mouse position to world coordinates
                double worldX = viewport.getX() + mouseX / currentScale;
                double worldY = viewport.getY() + mouseY / currentScale;
    
                // Apply new zoom
                viewport.setZoom(newScale);
    
                // Calculate new viewport position to keep mouse point fixed
                double newViewportX = worldX - mouseX / newScale;
                double newViewportY = worldY - mouseY / newScale;
    
                // Clamp position if necessary
                double maxX = Math.max(0, WORLD_WIDTH - (WINDOW_WIDTH / newScale));
                double maxY = Math.max(0, WORLD_HEIGHT - ((WINDOW_HEIGHT - BOTTOM_BAR_HEIGHT) / newScale));
    
                viewport.setX(FXGLMath.clamp((float) newViewportX, 0f, (float) maxX));
                viewport.setY(FXGLMath.clamp((float) newViewportY, 0f, (float) maxY));

                    // Update scrollbar ranges after zoom
                    updateScrollbarRanges();
                }
            } else {
                double delta = e.getDeltaY();
                double newValue = verticalScrollBar.getValue() - delta;
                verticalScrollBar.setValue(FXGLMath.clamp((float) newValue, 
                                                        (float) verticalScrollBar.getMin(), 
                                                        (float) verticalScrollBar.getMax()));

            }
        });
    }

    private void saveLayoutImage() {
        // Hide grid lines, sidebar, scrollbars, and highlight'
        menuBar.setVisible(false);
        gridLines.forEach(line -> line.setVisible(false));
        sidebar.setVisible(false);
        horizontalScrollBar.setVisible(false);
        verticalScrollBar.setVisible(false);
        if (highlightEntity != null) {
            highlightEntity.setVisible(false);
        }

        // Show cell labels for printing
        FXGL.getGameWorld().getEntities().stream()
            .filter(e -> e instanceof Furniture)
            .map(e -> (Furniture) e)
            .filter(f -> f.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR || f.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE)
            .forEach(Furniture::showCellLabel);

        // Create a FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Layout Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png")
        );

        // Show save dialog
        File file = fileChooser.showSaveDialog(FXGL.getGameScene().getRoot().getScene().getWindow());

        if (file != null) {
            try {
                // Calculate bounding rectangle of all furniture
                double minX = Double.MAX_VALUE;
                double minY = Double.MAX_VALUE;
                double maxX = Double.MIN_VALUE;
                double maxY = Double.MIN_VALUE;
                
                var furnitureEntities = FXGL.getGameWorld().getEntities().stream()
                    .filter(e -> e instanceof Furniture)
                    .collect(java.util.stream.Collectors.toList());
                
                if (!furnitureEntities.isEmpty()) {
                    for (var entity : furnitureEntities) {
                        minX = Math.min(minX, entity.getX());
                        minY = Math.min(minY, entity.getY());
                        maxX = Math.max(maxX, entity.getX() + CELL_SIZE);
                        maxY = Math.max(maxY, entity.getY() + CELL_SIZE);
                    }
                    
                    // Add margin (50 pixels on each side)
                    int margin = 50;
                    minX = Math.max(0, minX - margin);
                    minY = Math.max(0, minY - margin);
                    maxX = Math.min(WORLD_WIDTH, maxX + margin);
                    maxY = Math.min(WORLD_HEIGHT, maxY + margin);
                    
                    // Calculate dimensions
                    int width = (int) (maxX - minX);
                    int height = (int) (maxY - minY);
                    
                    // Add space for legend on the right (1000 pixels at 3x resolution = 3000 pixels)
                    int legendWidth = 1000 * 3;
                    int totalWidth = width * 3 + legendWidth;
                    int scaledHeight = height * 3;
                    
                    // Create a WritableImage with space for legend
                    WritableImage image = new WritableImage(totalWidth, scaledHeight);

                // Set up snapshot parameters
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.WHITE); // Set background color

                    // Set the viewport to capture only the furniture area
                    var viewport = FXGL.getGameScene().getViewport();
                    double originalX = viewport.getX();
                    double originalY = viewport.getY();
                    double originalZoom = viewport.getZoom();
                    
                    // Set viewport to capture the furniture area
                    viewport.setX(minX);
                    viewport.setY(minY);
                    viewport.setZoom(3.0); // 3x zoom for 3x resolution

                    // Take the snapshot of the layout area
                    WritableImage layoutImage = new WritableImage(width * 3, scaledHeight);
                    FXGL.getGameScene().getRoot().snapshot(params, layoutImage);
                    
                    // Create legend content
                    createLegendContent(image, layoutImage, width * 3, scaledHeight, legendWidth, furnitureEntities);

                    // Restore original viewport
                    viewport.setX(originalX);
                    viewport.setY(originalY);
                    viewport.setZoom(originalZoom);

                // Save the image
                ImageIO.write(
                    SwingFXUtils.fromFXImage(image, null),
                    "png",
                    file
                );
                } else {
                    // No furniture, save empty image
                    WritableImage image = new WritableImage(800, 600);
                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.WHITE);
                    FXGL.getGameScene().getRoot().snapshot(params, image);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save image: " + e.getMessage());
            } finally {
                // Show grid lines, sidebar, scrollbars, menu bar, and highlight again, hide cell labels
                gridLines.forEach(line -> line.setVisible(true));
                sidebar.setVisible(true);
                horizontalScrollBar.setVisible(true);
                verticalScrollBar.setVisible(true);
                menuBar.setVisible(true);
                if (highlightEntity != null) {
                    highlightEntity.setVisible(true);
                }
                
                // Hide cell labels again
                FXGL.getGameWorld().getEntities().stream()
                    .filter(e -> e instanceof Furniture)
                    .map(e -> (Furniture) e)
                    .filter(f -> f.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR || f.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE)
                    .forEach(Furniture::hideCellLabel);
            }
        } else {
            // If user cancels, make sure to show everything again and hide cell labels
            gridLines.forEach(line -> line.setVisible(true));
            sidebar.setVisible(true);
            horizontalScrollBar.setVisible(true);
            verticalScrollBar.setVisible(true);
            menuBar.setVisible(true);
            if (highlightEntity != null) {
                highlightEntity.setVisible(true);
            }
            
            // Hide cell labels again
            FXGL.getGameWorld().getEntities().stream()
                .filter(e -> e instanceof Furniture)
                .map(e -> (Furniture) e)
                .filter(f -> f.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR || f.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE)
                .forEach(Furniture::hideCellLabel);
        }
    }

    private void initContextMenu() {
        contextMenu = new VBox(5); // 5px spacing between items
        contextMenu.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-width: 1px;" +
            
            
            
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 2);"
        );
        contextMenu.setVisible(false);
        FXGL.addUINode(contextMenu, 0, 0);
    }

    private void showContextMenu(Furniture furniture, double mouseX, double mouseY) {
        contextMenu.getChildren().clear();
        hideTooltip();  // Hide any existing tooltip
        
        // Create assignment label with hover effect
        Label assignmentLabel = new Label("Assignment");
        assignmentLabel.setStyle(
            "-fx-padding: 5 10;" +
            "-fx-cursor: hand;" +
            "-fx-min-width: 100;"
        );
        
        // Add hover effect
        assignmentLabel.setOnMouseEntered(e -> 
            assignmentLabel.setStyle(
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;" +
                "-fx-min-width: 100;" +
                "-fx-background-color: #f0f0f0;"
            )
        );
        
        assignmentLabel.setOnMouseExited(e -> 
            assignmentLabel.setStyle(
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;" +
                "-fx-min-width: 100;"
            )
        );
        
        // Add click handler to open edit window
        assignmentLabel.setOnMouseClicked(e -> {
            hideContextMenu();
            EditWindow editWindow = new EditWindow(furniture);
            editWindow.show();
        });
        
        contextMenu.getChildren().add(assignmentLabel);
        
        // Position the context menu at mouse position
        double menuX = mouseX;
        double menuY = mouseY;
        
        // Ensure menu stays within window bounds
        if (menuX + contextMenu.getWidth() > FXGL.getAppWidth()) {
            menuX = FXGL.getAppWidth() - contextMenu.getWidth();
        }
        if (menuY + contextMenu.getHeight() > FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT) {
            menuY = FXGL.getAppHeight() - BOTTOM_BAR_HEIGHT - contextMenu.getHeight();
        }
        
        contextMenu.setTranslateX(menuX);
        contextMenu.setTranslateY(menuY);
        contextMenu.setVisible(true);
        isContextMenuOpen = true;
        
        // Ensure context menu is on top
        contextMenu.toFront();
    }

    private void hideContextMenu() {
        contextMenu.setVisible(false);
        isContextMenuOpen = false;
    }
    
    private void createLegendContent(WritableImage finalImage, WritableImage layoutImage, int layoutWidth, int layoutHeight, int legendWidth, List<Entity> furnitureEntities) {
        // Copy layout image to the left side of final image
        finalImage.getPixelWriter().setPixels(0, 0, layoutWidth, layoutHeight, layoutImage.getPixelReader(), 0, 0);
        
        // Create a horizontal layout for two columns
        javafx.scene.layout.HBox legendContainer = new javafx.scene.layout.HBox(40); // 40px spacing between columns
        legendContainer.setStyle("-fx-background-color: white; -fx-padding: 20;");
        legendContainer.setPrefWidth(legendWidth / 4.0); // Convert back to normal scale
        legendContainer.setAlignment(javafx.geometry.Pos.TOP_LEFT); // Align content to top left
        
        // Separate furniture into chairs and tables
        List<Furniture> chairs = new ArrayList<>();
        List<Furniture> tables = new ArrayList<>();
        
        for (var entity : furnitureEntities) {
            if (entity instanceof Furniture) {
                Furniture furniture = (Furniture) entity;
                if (furniture.getFurnitureType() == com.btp.layout.model.FurnitureType.CHAIR) {
                    chairs.add(furniture);
                } else if (furniture.getFurnitureType() == com.btp.layout.model.FurnitureType.TABLE) {
                    tables.add(furniture);
                }
            }
        }
        
        // Sort both lists by cell label
        chairs.sort((f1, f2) -> {
            return f1.getFurnitureLabel().compareTo(f2.getFurnitureLabel());
        });
        
        tables.sort((f1, f2) -> {
            return f1.getFurnitureLabel().compareTo(f2.getFurnitureLabel());
        });
        
        // Create Chair column
        javafx.scene.layout.VBox chairColumn = new javafx.scene.layout.VBox(10);
        chairColumn.setPrefWidth((legendWidth / 4.0 - 40) / 2.0); // Half width minus spacing
        
        // Add Chair heading
        javafx.scene.control.Label chairHeading = new javafx.scene.control.Label("Chair");
        chairHeading.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 48));
        chairHeading.setStyle("-fx-text-fill: black; -fx-underline: true;");
        chairColumn.getChildren().add(chairHeading);
        
        // Add chairs
        for (Furniture chair : chairs) {
            String cellLabel = chair.getFurnitureLabel();
            javafx.scene.control.Label mainLabel = new javafx.scene.control.Label(cellLabel);
            mainLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            mainLabel.setStyle("-fx-text-fill: black;");
            chairColumn.getChildren().add(mainLabel);
            
            // Add guest assignment
            String guest = chair.getAssignedGuest();
            if (guest != null && !guest.isEmpty()) {
                javafx.scene.control.Label guestLabel = new javafx.scene.control.Label("  " + guest);
                guestLabel.setFont(javafx.scene.text.Font.font("Arial", 36));
                guestLabel.setStyle("-fx-text-fill: black;");
                chairColumn.getChildren().add(guestLabel);
            } else {
                javafx.scene.control.Label noGuestLabel = new javafx.scene.control.Label("  -");
                noGuestLabel.setFont(javafx.scene.text.Font.font("Arial", 36));
                noGuestLabel.setStyle("-fx-text-fill: gray;");
                chairColumn.getChildren().add(noGuestLabel);
            }
            
            // Add spacing between entries
            chairColumn.getChildren().add(new javafx.scene.control.Label(""));
        }
        
        // Create Table column
        javafx.scene.layout.VBox tableColumn = new javafx.scene.layout.VBox(10);
        tableColumn.setPrefWidth((legendWidth / 4.0 - 40) / 2.0); // Half width minus spacing
        
        // Add Table heading
        javafx.scene.control.Label tableHeading = new javafx.scene.control.Label("Table");
        tableHeading.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 48));
        tableHeading.setStyle("-fx-text-fill: black; -fx-underline: true;");
        tableColumn.getChildren().add(tableHeading);
        
        // Add tables
        for (Furniture table : tables) {
            String cellLabel = table.getFurnitureLabel();
            javafx.scene.control.Label mainLabel = new javafx.scene.control.Label(cellLabel);
            mainLabel.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 36));
            mainLabel.setStyle("-fx-text-fill: black;");
            tableColumn.getChildren().add(mainLabel);
            
            // Add guest assignments
            List<String> guests = table.getAssignedGuests();
            if (guests != null && !guests.isEmpty()) {
                for (String guest : guests) {
                    javafx.scene.control.Label guestLabel = new javafx.scene.control.Label("  " + guest);
                    guestLabel.setFont(javafx.scene.text.Font.font("Arial", 36));
                    guestLabel.setStyle("-fx-text-fill: black;");
                    tableColumn.getChildren().add(guestLabel);
                }
            } else {
                javafx.scene.control.Label noGuestLabel = new javafx.scene.control.Label("  -");
                noGuestLabel.setFont(javafx.scene.text.Font.font("Arial", 36));
                noGuestLabel.setStyle("-fx-text-fill: gray;");
                tableColumn.getChildren().add(noGuestLabel);
            }
            
            // Add spacing between entries
            tableColumn.getChildren().add(new javafx.scene.control.Label(""));
        }
        
        // Add columns to container
        legendContainer.getChildren().addAll(chairColumn, tableColumn);
        
        // Create a scene to render the legend
        javafx.scene.Scene legendScene = new javafx.scene.Scene(legendContainer);
        legendScene.setFill(javafx.scene.paint.Color.WHITE);
        
        // Take snapshot of the legend
        javafx.scene.SnapshotParameters legendParams = new javafx.scene.SnapshotParameters();
        legendParams.setFill(javafx.scene.paint.Color.WHITE);
        WritableImage legendImage = legendContainer.snapshot(legendParams, null);
        
        // Scale the legend image to 4x resolution while maintaining aspect ratio
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(legendImage);
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        imageView.setSmooth(false); // Disable smoothing for pixel-perfect scaling
        
        // Calculate the scale factor to fit within the legend area
        double scaleX = (double) legendWidth / legendImage.getWidth();
        double scaleY = (double) layoutHeight / legendImage.getHeight();
        double scale = Math.min(scaleX, scaleY); // Use the smaller scale to fit within bounds
        
        // Set the size while preserving aspect ratio
        imageView.setFitWidth(legendImage.getWidth() * scale);
        imageView.setFitHeight(legendImage.getHeight() * scale);
        
        // Take snapshot of the properly scaled legend
        WritableImage scaledLegendImage = imageView.snapshot(legendParams, null);
        
        // Calculate horizontal centering offset (keep vertical at top)
        double offsetX = (legendWidth - scaledLegendImage.getWidth()) / 2.0;
        double offsetY = 0; // Start from top
        
        // Copy the scaled legend to the right side of the final image, centered horizontally, top-aligned
        finalImage.getPixelWriter().setPixels(
            (int)(layoutWidth + offsetX), 
            (int)offsetY, 
            (int)scaledLegendImage.getWidth(), 
            (int)scaledLegendImage.getHeight(), 
            scaledLegendImage.getPixelReader(), 
            0, 
            0
        );
    }

    @Override
    protected void initInput() {
        // Add Ctrl+Z handler for undo
        FXGL.getInput().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.Z) {
                handleUndo();
            } else if (e.isControlDown() && e.getCode() == KeyCode.S && e.isShiftDown()) {
                saveLayoutImage();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
} 