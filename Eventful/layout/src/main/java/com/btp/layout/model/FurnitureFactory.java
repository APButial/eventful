package com.btp.layout.model;

public class FurnitureFactory {
    public enum FurnitureType {
        CHAIR, TABLE, WALL, FLOOR, EDIT, ERASE
    }

    private static FurnitureType currentType = FurnitureType.CHAIR;

    public static void setCurrentType(FurnitureType type) {
        currentType = type;
    }

    public static FurnitureType getCurrentType() {
        return currentType;
    }

    public static Furniture createFurniture(int gridX, int gridY) {
        if (currentType == FurnitureType.EDIT || currentType == FurnitureType.ERASE) {
            return null;
        }
        
        return switch (currentType) {
            case CHAIR -> new Chair(gridX, gridY);
            case TABLE -> new Table(gridX, gridY);
            case WALL -> new Wall(gridX, gridY);
            case FLOOR -> new Floor(gridX, gridY);
            case EDIT, ERASE -> null; // This case won't be reached due to the check above
        };
    }
} 