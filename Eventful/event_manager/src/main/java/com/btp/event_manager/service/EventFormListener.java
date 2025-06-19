package com.btp.event_manager.service;

import java.io.IOException;

public interface EventFormListener {
    public void startDateUpdated();
    public void endDateUpdated();
    public void startTimeUpdated();
    public void endTimeUpdated();
    public void descriptionUpdated();
    public void guestsUpdated();
    public void sendEmail();
    public void onUpdate();
    public void onReturn();
    public void onBudgetTracker();
    public void onExport() throws IOException;
    public void onInbox();
    public void onDelete();
}
