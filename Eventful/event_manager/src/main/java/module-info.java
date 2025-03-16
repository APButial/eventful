module event_manager {
    requires login;
    requires budget_tracker;
    requires appfx;
    requires dashboard;
    requires java.mail;
    requires com.calendarfx.view;
    requires logs;
    requires kernel;
    requires layout;
    requires io;
    requires java.desktop;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}