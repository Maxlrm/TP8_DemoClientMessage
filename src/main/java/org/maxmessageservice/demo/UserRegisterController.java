package org.maxmessageservice.demo;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class UserRegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    public void initialize() {
        ServerConnectionController serverConnectionController = (ServerConnectionController) Launcher.getController("ServerConnection");
        socket = serverConnectionController.getSocket();
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void register() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        out.println("REGISTER " + username + " " + password);
        String response = in.nextLine();

        if (response.equals("SUCCESS")) {
            // Retourner à la fenêtre de connexion utilisateur
            Launcher.setRoot("UserLogin");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur d'inscription");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de créer le compte.");
            alert.showAndWait();
        }
    }
}