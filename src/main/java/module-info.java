module org.maxmessageservice.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens org.maxmessageservice.demo to javafx.fxml;
    exports org.maxmessageservice.demo;
}