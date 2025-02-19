module event_manager {
    requires login;
    requires budget_tracker;
    requires appfx;
    requires static lombok;
    requires dashboard;
    requires java.xml;
    requires javafx.controls;
    requires java.mail;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}