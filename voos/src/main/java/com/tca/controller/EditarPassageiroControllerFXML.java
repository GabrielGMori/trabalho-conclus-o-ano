package com.tca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditarPassageiroControllerFXML {

    @FXML
    private TextField senhaTextField;

    @FXML
    private ChoiceBox<?> nacionalidadeChoiceBox;

    @FXML
    private TextField passaporteTextField;

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
    void deletar(ActionEvent event) {

    }

    @FXML
    void confirmar(ActionEvent event) {

    }

}
