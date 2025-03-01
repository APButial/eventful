module dashboard {
    requires appfx;
    requires java.mail;
    requires com.calendarfx.view;
    requires budget_tracker;

    exports com.btp.dashboard.component to javafx.graphics, event_manager;
    exports com.btp.dashboard.service;
}