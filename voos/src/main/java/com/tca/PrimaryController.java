package com.tca;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PrimaryController {

    @FXML
    private ListView<HBox> listView; // Updated to hold HBox elements

    @FXML
    private Button primaryButton;

    @FXML
    void addHBox(ActionEvent event) {
        // Create a new HBox
        HBox newHBox = new HBox();
        newHBox.setStyle("-fx-background-color: red;"); // Set red background
        newHBox.setPrefHeight(50); // Set height to 50
        newHBox.setMaxWidth(Double.MAX_VALUE); // Allow full width in the ListView

        // Add some content to the HBox (optional)
        Text text = new Text("New Item");
        text.setStyle("-fx-fill: white; -fx-font-size: 14px;"); // Optional text styling
        newHBox.getChildren().add(text);

        // Add the new HBox to the ListView
        listView.getItems().add(newHBox);
    }
}
