package com.btp.layout.model;

public class Table extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/table.png";

    public Table(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH);
    }

    public Table(int gridX, int gridY, String imagePath) {
        super(gridX, gridY, imagePath, true, FurnitureType.TABLE);
    }

    // Copy constructor
    private Table(Table other) {
        super(other);
    }

    @Override
    public Furniture createCopy() {
        return new Table(this);
    }
} 