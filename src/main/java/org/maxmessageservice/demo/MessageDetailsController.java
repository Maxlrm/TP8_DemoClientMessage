package org.maxmessageservice.demo;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MessageDetailsController {

    @FXML
    private TextArea messageDetails;

    public void setMessageDetails(String message) {
        messageDetails.setText(message);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) messageDetails.getScene().getWindow();
        stage.close();
    }
}