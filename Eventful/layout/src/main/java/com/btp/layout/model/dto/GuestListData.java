package com.btp.layout.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class GuestListData {
    private final List<String> guests;
    private final Set<String> assignedToChairs;
    private final Set<String> assignedToTables;
    private final Map<String, String> guestToChairLabel;
    private final Map<String, String> guestToTableLabel;

    @JsonCreator
    public GuestListData(
        @JsonProperty("guests") List<String> guests,
        @JsonProperty("assignedToChairs") Set<String> assignedToChairs,
        @JsonProperty("assignedToTables") Set<String> assignedToTables,
        @JsonProperty("guestToChairLabel") Map<String, String> guestToChairLabel,
        @JsonProperty("guestToTableLabel") Map<String, String> guestToTableLabel
    ) {
        this.guests = guests;
        this.assignedToChairs = assignedToChairs;
        this.assignedToTables = assignedToTables;
        this.guestToChairLabel = guestToChairLabel;
        this.guestToTableLabel = guestToTableLabel;
    }

    public List<String> getGuests() {
        return guests;
    }

    public Set<String> getAssignedToChairs() {
        return assignedToChairs;
    }

    public Set<String> getAssignedToTables() {
        return assignedToTables;
    }

    public Map<String, String> getGuestToChairLabel() {
        return guestToChairLabel;
    }

    public Map<String, String> getGuestToTableLabel() {
        return guestToTableLabel;
    }
} 