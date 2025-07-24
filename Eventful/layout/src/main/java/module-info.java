module com.btp.layout {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.core;
    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    exports com.btp.layout.model.dto to com.fasterxml.jackson.databind;
    exports com.btp.layout;
    exports com.btp.layout.model;
    exports com.btp.layout.ui;

    opens com.btp.layout.model.dto to com.fasterxml.jackson.databind;
    
}