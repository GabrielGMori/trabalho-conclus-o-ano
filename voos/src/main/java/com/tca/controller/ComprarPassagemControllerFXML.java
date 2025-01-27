package com.tca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ComprarPassagemControllerFXML {

    @FXML
    private Pane comprarPassagemPane;

    @FXML
    private VBox assentosVBox;

    @FXML
    private Pane desejaReservarAssentoPane;

    @FXML
    private Text selecionadoText;

    @FXML
    private ChoiceBox<?> metodoPagamentoChoiceBox;

    @FXML
    private Pane reservarAssentoPane;

    @FXML
    void cancelarEscolherAssento(ActionEvent event) {

    }

    @FXML
    void naoEscolherAssento(ActionEvent event) {

    }

    @FXML
    void simEscolherAssento(ActionEvent event) {

    }

    @FXML
    void cancelarAssento(ActionEvent event) {

    }

    @FXML
    void confirmarAssento(ActionEvent event) {

    }

    @FXML
    void cancelarPagamento(ActionEvent event) {

    }

    @FXML
    void confirmarPagamento(ActionEvent event) {

    }

}
