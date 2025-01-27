package com.tca.controller;

import java.io.IOException;

import com.tca.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EscolherLoginControllerFXML {

    @FXML
    void passageiro(ActionEvent event) throws IOException {
        App.setRoot("loginPassageiro");
    }

    @FXML
    void administrador(ActionEvent event) throws IOException {
        App.setRoot("loginAdministrador");
    }

}
