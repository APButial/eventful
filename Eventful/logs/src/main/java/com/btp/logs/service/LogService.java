package com.btp.logs.service;

public interface LogService {
    public void _updateLogs(String text);
    public String _loadLogs();

    public void _loggedIn();
    public void _eventsLoaded();

    public void _accessedEvent();
    public void _createdEvent();
    public void _savedEvent();


    public void _startDateUpdated();
    public void _endDateUpdated();
    public void _startTimeUpdated();
    public void _endTimeUpdated();
    public void _descriptionUpdated();
    public void _guestsInvited();

    public void _budgetTrackerUpdated();
    public void _printedTimeline();
}
