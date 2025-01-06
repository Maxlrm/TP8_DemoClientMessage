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

public class UserLoginController {

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
            if (socket == null || socket.isClosed()) {
                String ip = serverConnectionController.getIp();
                int port = serverConnectionController.getPort();
                socket = new Socket(ip, port);
                serverConnectionController.setSocket(socket);
            }
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        out.println("LOGIN " + username + " " + password);
        String response = in.nextLine();

        if (response.equals("SUCCESS")) {
            // Afficher la fenêtre de messagerie
            Launcher.setRoot("Messaging");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de connexion");
            alert.setHeaderText(null);
            alert.setContentText("Nom d'utilisateur ou mot de passe incorrect.");
            alert.showAndWait();
        }
    }

    @FXML
    private void showRegister() throws IOException {
        // Afficher la fenêtre d'inscription
        Launcher.setRoot("UserRegister");
    }
}