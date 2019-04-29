module monopoly {
    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.web;

    requires org.junit.jupiter.api;
    requires org.testfx;
    requires org.testfx.junit5;

    exports engine;
}