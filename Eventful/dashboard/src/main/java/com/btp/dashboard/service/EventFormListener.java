package com.btp.dashboard.service;

public interface EventFormListener {
    public void startDateUpdated();
    public void endDateUpdated();
    public void startTimeUpdated();
    public void endTimeUpdated();
    public void descriptionUpdated();
    public void guestsUpdated();
    public void onUpdate();
    public void onReturn();
}
