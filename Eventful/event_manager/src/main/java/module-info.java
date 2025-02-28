module event_manager {
    requires login;
    requires budget_tracker;
    requires appfx;
    requires static lombok;
    requires dashboard;
    requires java.mail;
    requires com.calendarfx.view;
    requires java.xml;
    requires logs;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}