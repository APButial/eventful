module dashboard {
    requires appfx;
    requires javafx.controls;
    requires java.mail;

    exports com.btp.dashboard.component to javafx.graphics, event_manager;
    exports com.btp.dashboard.service;
}