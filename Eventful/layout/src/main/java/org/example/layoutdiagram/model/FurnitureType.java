package org.example.layoutdiagram.model;

import javafx.scene.paint.Color;

public enum FurnitureType {
    CHAIR(Color.GREEN),
    TABLE(Color.RED),
    WALL(Color.YELLOW),
    FLOOR(Color.BLUE);

    private final Color color;

    FurnitureType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
} 