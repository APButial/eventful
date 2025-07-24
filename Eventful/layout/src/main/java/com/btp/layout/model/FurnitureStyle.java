package com.btp.layout.model;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class FurnitureStyle {
    private static final Map<FurnitureFactory.FurnitureType, List<String>> styleMap = new HashMap<>();
    private static String currentStyle = null;

    static {
        // Initialize with default styles
        List<String> chairStyles = new ArrayList<>();
        chairStyles.add("/com/btp/layout/images/chair.png");
        chairStyles.add("/com/btp/layout/images/chair_modern.png");
        chairStyles.add("/com/btp/layout/images/chair_vintage.png");
        styleMap.put(FurnitureFactory.FurnitureType.CHAIR, chairStyles);

        List<String> tableStyles = new ArrayList<>();
        tableStyles.add("/com/btp/layout/images/table.png");
        tableStyles.add("/com/btp/layout/images/table_round.png");
        tableStyles.add("/com/btp/layout/images/table_square.png");
        styleMap.put(FurnitureFactory.FurnitureType.TABLE, tableStyles);

        List<String> wallStyles = new ArrayList<>();
        wallStyles.add("/com/btp/layout/images/wall.png");
        wallStyles.add("/com/btp/layout/images/wall_brick.png");
        wallStyles.add("/com/btp/layout/images/wall_modern.png");
        styleMap.put(FurnitureFactory.FurnitureType.WALL, wallStyles);

        List<String> floorStyles = new ArrayList<>();
        floorStyles.add("/com/btp/layout/images/floor.png");
        floorStyles.add("/com/btp/layout/images/floor_wood.png");
        floorStyles.add("/com/btp/layout/images/floor_tile.png");
        styleMap.put(FurnitureFactory.FurnitureType.FLOOR, floorStyles);
    }

    public static List<String> getStylesForType(FurnitureFactory.FurnitureType type) {
        return styleMap.getOrDefault(type, new ArrayList<>());
    }

    public static String getCurrentStyle() {
        return currentStyle;
    }

    public static void setCurrentStyle(String style) {
        currentStyle = style;
    }

    public static String getDefaultStyleForType(FurnitureFactory.FurnitureType type) {
        List<String> styles = styleMap.get(type);
        return styles != null && !styles.isEmpty() ? styles.get(0) : null;
    }
} 