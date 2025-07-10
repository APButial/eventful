package com.btp.layout.model;

public class FurnitureFactory {
    public enum FurnitureType {
        CHAIR, TABLE, WALL, FLOOR, EDIT, ERASE
    }

    private static FurnitureType currentType = FurnitureType.CHAIR;

    public static void setCurrentType(FurnitureType type) {
        currentType = type;
        // Set default style for the selected type
        FurnitureStyle.setCurrentStyle(FurnitureStyle.getDefaultStyleForType(type));
    }

    public static FurnitureType getCurrentType() {
        return currentType;
    }

    public static Furniture createFurniture(int gridX, int gridY) {
        if (currentType == FurnitureType.EDIT || currentType == FurnitureType.ERASE) {
            return null;
        }
        
        String currentStyle = FurnitureStyle.getCurrentStyle();
        if (currentStyle == null) {
            currentStyle = FurnitureStyle.getDefaultStyleForType(currentType);
        }

        return switch (currentType) {
            case CHAIR -> new Chair(gridX, gridY, currentStyle);
            case TABLE -> new Table(gridX, gridY, currentStyle);
            case WALL -> new Wall(gridX, gridY, currentStyle);
            case FLOOR -> new Floor(gridX, gridY, currentStyle);
            case EDIT, ERASE -> null; // This case won't be reached due to the check above
        };
    }
} 