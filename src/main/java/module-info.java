module erp.poc {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    opens app to javafx.fxml;
    opens app.ui to javafx.fxml;

    exports app;
    exports app.ui;
    exports data;
    exports domain;
}
