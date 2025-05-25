package com.btp.event_manager.service;

public interface DashNavigateListener {
    void createEventTriggered();
    void myEventsTriggered();
    void eventTimelineTriggered();
    void logsTriggered();
    void logoTriggered();
    void returnTriggered();
    void budgetUpdateTriggered();
    void accountTriggered();
}
