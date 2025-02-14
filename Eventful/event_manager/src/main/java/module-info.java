module event_manager {
    requires javafx.controls;
    requires login;
    requires budget_tracker;
    requires appfx;
    requires static lombok;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}