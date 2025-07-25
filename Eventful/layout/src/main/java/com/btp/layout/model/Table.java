package com.btp.layout.model;

public class Table extends Furniture {
    private static final String DEFAULT_IMAGE_PATH = "/com/btp/layout/images/table.png";

    public Table(int gridX, int gridY, String imagePath, String label) {
        super(gridX, gridY, imagePath, true, FurnitureType.TABLE, label);
    }

    public Table(int gridX, int gridY, String imagePath) {
        this(gridX, gridY, imagePath, null);
    }

    public Table(int gridX, int gridY) {
        this(gridX, gridY, DEFAULT_IMAGE_PATH, null);
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