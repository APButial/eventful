package com.btp.layout.model;

public class FurnitureFactory {
    public enum FurnitureType {
        CHAIR, TABLE, WALL, FLOOR, EDIT, ERASE
    }

    private static FurnitureType currentType = FurnitureType.CHAIR;
    private static int chairCount = 0;
    private static int tableCount = 0;

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

        switch (currentType) {
            case CHAIR -> {
                chairCount++;
                return new Chair(gridX, gridY, currentStyle, "C" + chairCount);
            }
            case TABLE -> {
                tableCount++;
                return new Table(gridX, gridY, currentStyle, "T" + tableCount);
            }
            case WALL -> {
                return new Wall(gridX, gridY, currentStyle);
            }
            case FLOOR -> {
                return new Floor(gridX, gridY, currentStyle);
            }
            default -> {
                return null;
            }
        }
    }

    // Call this after erasing or undoing erase to relabel all chairs/tables sequentially
    public static void relabelFurniture(java.util.List<com.almasb.fxgl.entity.Entity> entities) {
        int c = 1;
        int t = 1;
        for (var entity : entities) {
            if (entity instanceof Chair) {
                ((Chair) entity).setFurnitureLabel("C" + c);
                c++;
            } else if (entity instanceof Table) {
                ((Table) entity).setFurnitureLabel("T" + t);
                t++;
            }
        }
        chairCount = c - 1;
        tableCount = t - 1;
    }

    public static void decrementChairCount() {
        if (chairCount > 0) chairCount--;
    }
    public static void decrementTableCount() {
        if (tableCount > 0) tableCount--;
    }
    public static void incrementChairCount() {
        chairCount++;
    }
    public static void incrementTableCount() {
        tableCount++;
    }
    public static int getChairCount() { return chairCount; }
    public static int getTableCount() { return tableCount; }
} 