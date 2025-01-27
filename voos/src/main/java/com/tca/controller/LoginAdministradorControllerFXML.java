package com.tca.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.tca.App;
import com.tca.model.Administrador;
import com.tca.model.Passageiro;
import com.tca.repository.AdministradorRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginAdministradorControllerFXML {
    private AdministradorRepository administradorRepository = new AdministradorRepository();

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    @FXML
    void voltar(ActionEvent event) throws IOException {
        App.setRoot("escolherLogin");
    }

    @FXML
    void login(ActionEvent event) {

    }

    private Boolean verificarCamposVazios() {
        if (senhaTextField.getText().isEmpty() || nomeTextField.getText().isEmpty() || cpfTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (cpfTextField.getText().length() != 11) {
            warningText.setText("O CPF digitado é inválido");
            return false;
        }
        return true;
    }

    private Passageiro encontrarAdministrador() {
        try {
            ArrayList<?> administradores = administradorRepository.get(App.formatNumericData(cpfTextField.getText().trim()));
            if (administradores.isEmpty()) {
                warningText.setText("Algum dado está incorreto");
                return null;
            } else if (administradores.size() > 1)  {
                warningText.setText("Duas ou mais contas foram encontradas com CPF digitado\nPor favor contate o suporte!");
                return null;
            }
            Passageiro passageiroEncontrado = (Passageiro) administradores.get(0);
            if (!passageiroEncontrado.getSenha().equals(senhaTextField.getText())) {
                warningText.setText("Algum dado está incorreto");
                return null;
            }
            return passageiroEncontrado;
        } catch(Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return null;
        }
    }

}
