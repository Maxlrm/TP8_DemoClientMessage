package org.maxmessageservice.demo;


import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessagingController {

    @FXML
    private ListView<String> messageList;

    @FXML
    private TextArea messageContent;

    @FXML
    private TextField recipientField;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private boolean showingReceivedMessages = true;

    public void initialize() {
        ServerConnectionController serverConnectionController = (ServerConnectionController) Launcher.getController("ServerConnection");
        socket = serverConnectionController.getSocket();
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            loadMessages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String selectedMessage = messageList.getSelectionModel().getSelectedItem();
                if (selectedMessage != null) {
                    showMessageDetails(selectedMessage);
                }
            }
        });
    }

    @FXML
    private void sendMessage() {
        String recipient = recipientField.getText();
        String content = messageContent.getText();

        out.println("SEND " + recipient + " " + content.replace("\n", "\\n"));
        String response = in.nextLine();

        if (response.equals("SUCCESS")) {
            // Mettre à jour la liste des messages
            if (!showingReceivedMessages) {
                messageList.getItems().add("To: " + recipient + "\n" + getExcerpt(content));
            }
            messageContent.clear();
            recipientField.clear();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur d'envoi");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'envoyer le message.");
            alert.showAndWait();
        }
    }

    @FXML
    private void logout() {
        try {
            out.println("LOGOUT");
            socket.close();
            Launcher.setRoot("UserLogin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteMessage() {
        String selectedMessage = messageList.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            out.println("DELETE " + selectedMessage);
            String response = in.nextLine();
            if (response.equals("SUCCESS")) {
                messageList.getItems().remove(selectedMessage);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur de suppression");
                alert.setHeaderText(null);
                alert.setContentText("Impossible de supprimer le message.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void showReceivedMessages() {
        showingReceivedMessages = true;
        loadMessages();
    }

    @FXML
    private void showSentMessages() {
        showingReceivedMessages = false;
        loadMessages();
    }

    private void loadMessages() {
        messageList.getItems().clear();
        out.println("LOAD_MESSAGES " + (showingReceivedMessages ? "RECEIVED" : "SENT"));
        while (in.hasNextLine()) {
            String message = in.nextLine();
            if (message.equals("END_OF_MESSAGES")) {
                break;
            }
            messageList.getItems().add(formatMessageForList(message));
        }
    }

    private void showMessageDetails(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MessageDetails.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            MessageDetailsController controller = loader.getController();
            controller.setMessageDetails(message);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatMessageForList(String message) {
        String[] parts = message.split(",", 4);
        if (parts.length < 4) {
            return message; // Return the original message if it doesn't have enough parts
        }
        String sender = parts[1];
        String content = parts[3].replace("\\n", "\n");
        return sender + ": " + getExcerpt(content);
    }

    private String getExcerpt(String content) {
        return content.length() > 30 ? content.substring(0, 30) + "..." : content;
    }
}
