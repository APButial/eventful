module login {
    requires javafx.controls;
    requires static lombok;
    requires appfx;
    requires jaxb.api;

    exports com.btp.login.components;
    exports com.btp.login.service;
}