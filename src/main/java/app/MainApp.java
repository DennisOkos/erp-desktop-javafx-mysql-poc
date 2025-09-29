package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import app.ui.CustomerView;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        CustomerView view = new CustomerView();
        Scene scene = new Scene(view, 900, 500);
        primaryStage.setTitle("ERP PoC - Customer CRUD");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
