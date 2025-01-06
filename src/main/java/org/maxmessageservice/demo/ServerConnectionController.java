package org.maxmessageservice.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.net.Socket;

public class ServerConnectionController {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    private Socket socket;

    @FXML
    private void connectToServer() {
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());

        try {
            socket = new Socket(ip, port);
            // Afficher la fenêtre de connexion utilisateur
            Launcher.setRoot("UserLogin");
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de se connecter au serveur.");
            alert.showAndWait();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIp() {
        return ipField.getText();
    }

    public int getPort() {
        return Integer.parseInt(portField.getText());
    }
}