package com.btp.layout.model;

public class Table extends Furniture {
    private static final String IMAGE_PATH = "/com/btp/layout/images/table.png";

    public Table(int gridX, int gridY) {
        super(gridX, gridY, IMAGE_PATH, true);
    }
} 