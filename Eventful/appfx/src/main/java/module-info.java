module appfx {
    requires static lombok;
    requires javafx.graphics;
    requires javafx.controls;
    exports com.btp.appfx.enums;
    exports com.btp.appfx.service;
    exports com.btp.appfx.model;
}