module event_manager {
    requires login;
    requires budget;
    requires appfx;
    requires dashboard;
    requires java.mail;
    requires logs;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}