module event_manager {
    requires login;
    requires budget;
    requires appfx;
    requires logs;
    requires kernel;
    requires layout;
    requires io;
    requires javafx.graphics;
    requires javafx.controls;
    requires java.desktop;
    requires com.calendarfx.view;
    requires com.google.api.client.auth;
    requires com.google.api.client;
    requires com.google.api.client.extensions.java6.auth;
    requires google.api.client;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.gmail;
    requires com.google.gson;
    requires mail;
    requires activation;
    requires org.apache.commons.codec;

    exports com.btp.event_manager;
    exports com.btp.event_manager.component;
}