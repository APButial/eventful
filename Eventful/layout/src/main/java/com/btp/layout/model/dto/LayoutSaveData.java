package com.btp.layout.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class LayoutSaveData {
    private final List<FurnitureData> furniture;
    private final GuestListData guestList;
    private final int chairCount;
    private final int tableCount;

    @JsonCreator
    public LayoutSaveData(
        @JsonProperty("furniture") List<FurnitureData> furniture,
        @JsonProperty("guestList") GuestListData guestList,
        @JsonProperty("chairCount") int chairCount,
        @JsonProperty("tableCount") int tableCount
    ) {
        this.furniture = furniture;
        this.guestList = guestList;
        this.chairCount = chairCount;
        this.tableCount = tableCount;
    }

    public List<FurnitureData> getFurniture() {
        return furniture;
    }

    public GuestListData getGuestList() {
        return guestList;
    }

    public int getChairCount() {
        return chairCount;
    }

    public int getTableCount() {
        return tableCount;
    }
} 