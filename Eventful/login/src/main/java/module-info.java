module login {
    requires appfx;
    requires java.compiler;
    requires javafx.controls;
    requires java.xml;

    exports com.btp.login.components;
    exports com.btp.login.service;
}