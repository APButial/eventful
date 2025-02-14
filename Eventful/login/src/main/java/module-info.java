module login {
    requires javafx.controls;
    requires static lombok;
    requires appfx;
    requires java.desktop;
    requires java.compiler;

    exports com.btp.login.components;
    exports com.btp.login.service;
}