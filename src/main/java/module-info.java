module org.example.librarymanagementsys {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    // Keep existing opens/exports
    opens org.example.librarymanagementsys to javafx.fxml;
    exports org.example.librarymanagementsys;
    opens Controller.Views to javafx.fxml;
    exports Controller.Views;

    // Add these new lines for Entity package
    opens Entity to javafx.base;    // This is needed for PropertyValueFactory
    exports Entity;                 // This exports the Entity package

    // Also open the Enums package
    opens Entity.Enums to javafx.base;
    exports Entity.Enums;
}