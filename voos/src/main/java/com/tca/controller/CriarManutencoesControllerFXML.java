package com.tca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarManutencoesControllerFXML {

    @FXML
    private TextField inicioDateTextField;

    @FXML
    private TextField inicioTimeTextField;

    @FXML
    private TextField fimTimeTextField;

    @FXML
    private ChoiceBox<?> aeronaveChoiceBox;

    @FXML
    private TextField fimDateTextField;

    @FXML
    private Text warningText;

    @FXML
    private TextField descricaoTextField;

    @FXML
    void cancelar(ActionEvent event) {

    }

    @FXML
    void criar(ActionEvent event) {

    }

}
