package com.tca.controller;

import java.io.IOException;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Administrador;
import com.tca.repository.AdministradorRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarAdministradorControllerFXML {
    private AdministradorRepository administradorRepository = AdministradorRepository.getInstance();

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAdministradores");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true && cpfEmUso() == false) {
            try {
                Resultado result = administradorRepository.criar(getAdministrador());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoAdministradores");
        }
    }

    private Boolean verificarCamposVazios() {
        if (cpfTextField.getText().isEmpty() ||
            nomeTextField.getText().isEmpty() ||
            senhaTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (!StringFormatter.formatNumericData(cpfTextField.getText()).matches("\\d{11}")) {
            warningText.setText("Algum dado digitado é inválido");
            return false;
        }
        return true;
    }

    private Boolean cpfEmUso() {
        Administrador administrador = getAdministrador();
        try {
            ArrayList<?> administradores = administradorRepository.getAdministradoresFiltro(administrador.getCpf(), null);
            if (!administradores.isEmpty()) {
                warningText.setText("CPF já registrado");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return true;
        }
    }

    private Administrador getAdministrador() {
        String cpf = StringFormatter.formatNumericData(cpfTextField.getText());
        String nome = StringFormatter.capitalize(nomeTextField.getText());
        String senha = senhaTextField.getText().trim();

        return new Administrador(cpf, nome, senha);
    }

}
