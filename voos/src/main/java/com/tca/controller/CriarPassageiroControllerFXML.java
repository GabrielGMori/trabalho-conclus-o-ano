package com.tca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarPassageiroControllerFXML {

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField passaporteTextField;

    @FXML
    private ChoiceBox<?> nacionalidadeChoiceBox;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telefoneTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void criar(ActionEvent event) {

    }

}
