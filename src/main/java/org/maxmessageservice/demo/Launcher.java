package org.maxmessageservice.demo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Launcher extends Application {

    private static Stage primaryStage;
    private static Map<String, Object> controllers = new HashMap<>();

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        setRoot("ServerConnection");
        primaryStage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(fxml + ".fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        controllers.put(fxml, loader.getController());
    }

    public static Object getController(String fxml) {
        return controllers.get(fxml);
    }

    public static void main(String[] args) {
        launch();
    }
}