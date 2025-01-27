package com.tca.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.tca.App;
import com.tca.model.Passageiro;
import com.tca.repository.PassageiroRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginPassageiroControllerFXML {
    private PassageiroRepository passageiroRepository = new PassageiroRepository();

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Text warningText;

    @FXML
    void voltar(ActionEvent event) throws IOException {
        App.setRoot("escolherLogin");
    }

    @FXML
    void criarConta(ActionEvent event) throws IOException {
        App.setRoot("cadastroPassageiro");
    }

    @FXML
    void login(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            Passageiro passageiro = encontrarPassageiro();
            if (passageiro == null) return;
            App.setUsuario(passageiro);
            App.setRoot("voosPassageiro");
        }
    }

    private Boolean verificarCamposVazios() {
        if (emailTextField.getText().isEmpty() || senhaTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (!(emailTextField.getText().contains("@"))) {
            warningText.setText("O e-mail inserido não é válido");
            return false;
        }
        return true;
    }

    private Passageiro encontrarPassageiro() {
        try {
            ArrayList<?> passageiros = passageiroRepository.getPassageirosFiltro(null, emailTextField.getText().trim().toLowerCase(), null, null, null, null);
            if (passageiros.isEmpty()) {
                warningText.setText("O e-mail ou senha estão incorretos");
                return null;
            } else if (passageiros.size() > 1)  {
                warningText.setText("Duas ou mais contas foram encontradas com e-mail digitado\nPor favor contate o suporte!");
                return null;
            }
            Passageiro passageiroEncontrado = (Passageiro) passageiros.get(0);
            if (!passageiroEncontrado.getSenha().equals(senhaTextField.getText())) {
                warningText.setText("O e-mail ou senha estão incorretos");
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
