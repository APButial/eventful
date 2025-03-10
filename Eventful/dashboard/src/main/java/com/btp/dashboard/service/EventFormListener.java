package com.btp.dashboard.service;

import javax.mail.internet.AddressException;

public interface EventFormListener {
    public void startDateUpdated();
    public void endDateUpdated();
    public void startTimeUpdated();
    public void endTimeUpdated();
    public void descriptionUpdated();
    public void guestsUpdated();
    public void sendEmail() throws AddressException;
    public void onUpdate();
    public void onReturn();
    public void onBudgetTracker();
}
