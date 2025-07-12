package com.btp.layout.model;

import com.almasb.fxgl.dsl.FXGL;
import com.btp.layout.model.dto.FurnitureData;
import com.btp.layout.model.dto.GuestListData;
import com.btp.layout.model.dto.LayoutSaveData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.almasb.fxgl.entity.Entity;
import com.btp.layout.model.commands.CommandManager;

public class LayoutManager {
    private static LayoutManager instance;
    private final ObjectMapper objectMapper;

    private LayoutManager() {
        objectMapper = new ObjectMapper();
    }

    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }

    public void saveLayout(String filePath) throws IOException {
        // Collect all furniture data
        List<FurnitureData> furnitureDataList = FXGL.getGameWorld().getEntities().stream()
            .filter(e -> e instanceof Furniture)
            .map(e -> {
                Furniture f = (Furniture) e;
                return new FurnitureData(
                    f.getFurnitureType(),
                    f.getGridX(),
                    f.getGridY(),
                    f.getFurnitureRotation(),
                    f.getImagePath(),
                    f.getAssignedGuest(),
                    f.getAssignedGuests(),
                    f.getFurnitureLabel()
                );
            })
            .collect(Collectors.toList());

        // Collect guest list data
        GuestList guestList = GuestList.getInstance();
        Map<String, String> guestToChairLabel = new HashMap<>();
        Map<String, String> guestToTableLabel = new HashMap<>();

        // Build guest-to-furniture mappings
        for (String guest : guestList.getAllGuests()) {
            Furniture chair = guestList.getAssignedChair(guest);
            if (chair != null) {
                guestToChairLabel.put(guest, chair.getFurnitureLabel());
            }
            Table table = guestList.getAssignedTable(guest);
            if (table != null) {
                guestToTableLabel.put(guest, table.getFurnitureLabel());
            }
        }

        GuestListData guestListData = new GuestListData(
            guestList.getAllGuests(),
            new HashSet<>(guestList.getUnassignedGuestsForChair()),
            new HashSet<>(guestList.getUnassignedGuestsForTable()),
            guestToChairLabel,
            guestToTableLabel
        );

        // Create save data object
        LayoutSaveData saveData = new LayoutSaveData(
            furnitureDataList,
            guestListData,
            FurnitureFactory.getChairCount(),
            FurnitureFactory.getTableCount()
        );

        // Write to file
        objectMapper.writeValue(new File(filePath), saveData);
    }

    public void loadLayout(String filePath) throws IOException {
        // Read from file
        LayoutSaveData saveData = objectMapper.readValue(new File(filePath), LayoutSaveData.class);

        // Clear existing layout - collect entities to remove first to avoid ConcurrentModificationException
        List<Entity> toRemove = FXGL.getGameWorld().getEntities().stream()
            .filter(e -> e instanceof Furniture)
            .collect(Collectors.toList());
        
        toRemove.forEach(FXGL.getGameWorld()::removeEntity);

        CommandManager.getInstance().clearUndoStack();

        // Reset guest list
        GuestList guestList = GuestList.getInstance();
        guestList.clear();

        // Restore furniture
        Map<String, Furniture> labelToFurniture = new HashMap<>();
        for (FurnitureData fd : saveData.getFurniture()) {
            Furniture furniture = null;
            switch (fd.getType()) {
                case CHAIR -> {
                    furniture = new Chair(fd.getGridX(), fd.getGridY(), fd.getImagePath(), fd.getLabel());
                    labelToFurniture.put(fd.getLabel(), furniture);
                }
                case TABLE -> {
                    furniture = new Table(fd.getGridX(), fd.getGridY(), fd.getImagePath(), fd.getLabel());
                    labelToFurniture.put(fd.getLabel(), furniture);
                }
                case WALL -> furniture = new Wall(fd.getGridX(), fd.getGridY(), fd.getImagePath());
                case FLOOR -> furniture = new Floor(fd.getGridX(), fd.getGridY(), fd.getImagePath());
            }

            if (furniture != null) {
                furniture.setRotation(fd.getRotation());
                if (fd.getAssignedGuest() != null) {
                    furniture.setAssignedGuest(fd.getAssignedGuest());
                }
                if (fd.getAssignedGuests() != null) {
                    fd.getAssignedGuests().forEach(furniture::addAssignedGuest);
                }
                FXGL.getGameWorld().addEntity(furniture);
            }
        }

        // Restore guest list
        GuestListData gld = saveData.getGuestList();
        gld.getGuests().forEach(guestList::addGuest);

        // Restore guest assignments
        gld.getGuestToChairLabel().forEach((guest, label) -> {
            Furniture chair = labelToFurniture.get(label);
            if (chair != null) {
                guestList.assignGuestToChair(guest, chair);
            }
        });

        gld.getGuestToTableLabel().forEach((guest, label) -> {
            Table table = (Table) labelToFurniture.get(label);
            if (table != null) {
                guestList.assignGuestToTable(guest, table);
            }
        });

        // Restore counters
        FurnitureFactory.relabelFurniture(FXGL.getGameWorld().getEntities().stream().toList());
    }
} 