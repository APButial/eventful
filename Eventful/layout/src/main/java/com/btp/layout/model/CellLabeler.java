package com.btp.layout.model;

public class CellLabeler {
    private static final String ROW_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+-=[]{}|;':\",./<>?";
    
    /**
     * Converts grid coordinates to a cell label
     * @param gridX column (0-59)
     * @param gridY row (0-59)
     * @return cell label like "a1", "b2", "A1", "!1", etc.
     */
    public static String getCellLabel(int gridX, int gridY) {
        if (gridX < 0 || gridX >= 60 || gridY < 0 || gridY >= 60) {
            return "??";
        }
        
        char rowChar = ROW_CHARS.charAt(gridY);
        int column = gridX + 1; // Convert to 1-based indexing
        
        return String.valueOf(rowChar) + column;
    }
    
    /**
     * Converts a cell label back to grid coordinates
     * @param cellLabel like "a1", "b2", "A1", "!1", etc.
     * @return int array [gridX, gridY] or null if invalid
     */
    public static int[] getGridCoordinates(String cellLabel) {
        if (cellLabel == null || cellLabel.length() < 2) {
            return null;
        }
        
        char rowChar = cellLabel.charAt(0);
        String columnStr = cellLabel.substring(1);
        
        try {
            int column = Integer.parseInt(columnStr);
            int rowIndex = ROW_CHARS.indexOf(rowChar);
            
            if (rowIndex == -1 || column < 1 || column > 60) {
                return null;
            }
            
            return new int[]{column - 1, rowIndex}; // Convert back to 0-based indexing
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Gets the character for a specific row index
     * @param rowIndex 0-based row index
     * @return character for the row
     */
    public static char getRowChar(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= ROW_CHARS.length()) {
            return '?';
        }
        return ROW_CHARS.charAt(rowIndex);
    }
    
    /**
     * Gets the total number of available row characters
     * @return number of available row characters
     */
    public static int getAvailableRows() {
        return ROW_CHARS.length();
    }
} 